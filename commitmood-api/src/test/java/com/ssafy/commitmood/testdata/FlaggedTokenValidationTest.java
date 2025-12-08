package com.ssafy.commitmood.testdata;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("testdata")
class FlaggedTokenValidationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static class TokenRow {
        long commitId;
        String token;
        String message;
    }

    @Test
    void flagged_token은_실제_commit_message에_포함되어야_한다() {
        List<TokenRow> rows = jdbcTemplate.query(
                """
                SELECT ft.COMMIT_LOG_ID, ft.TOKEN, c.MESSAGE
                FROM FLAGGED_TOKEN ft
                JOIN COMMIT_LOG c ON ft.COMMIT_LOG_ID = c.ID
                """,
                (rs, rowNum) -> {
                    TokenRow r = new TokenRow();
                    r.commitId = rs.getLong("COMMIT_LOG_ID");
                    r.token = rs.getString("TOKEN");
                    r.message = rs.getString("MESSAGE");
                    return r;
                }
        );

        assertThat(rows).isNotEmpty();

        for (TokenRow row : rows) {
            String msg = row.message == null ? "" : row.message.toLowerCase();
            String token = row.token == null ? "" : row.token.toLowerCase();
            assertThat(msg)
                    .as("commit %d message should contain token '%s'", row.commitId, token)
                    .contains(token);
        }
    }

    @Test
    void commit당_flagged_token은_최대_3개여야_한다() {
        Integer maxCount = jdbcTemplate.queryForObject(
                """
                SELECT MAX(cnt) AS max_cnt
                FROM (
                    SELECT COMMIT_LOG_ID, COUNT(*) AS cnt
                    FROM FLAGGED_TOKEN
                    GROUP BY COMMIT_LOG_ID
                )
                """,
                Integer.class
        );

        if (maxCount != null) {
            assertThat(maxCount).isLessThanOrEqualTo(3);
        }
    }

    @Test
    void negative_sentiment_score와_label이_일관되어야_한다() {
        // score < -0.3 이면서 SENTIMENT != 'NEGATIVE' 인 레코드가 있는지 확인
        Long mismatchCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM COMMIT_ANALYSIS
                WHERE SENTIMENT_SCORE < -0.3
                  AND SENTIMENT <> 'NEGATIVE'
                """,
                Long.class
        );

        assertThat(mismatchCount).isZero();
    }
}