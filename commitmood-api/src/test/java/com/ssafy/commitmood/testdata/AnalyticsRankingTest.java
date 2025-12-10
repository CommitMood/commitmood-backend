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
class AnalyticsRankingTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static class UserCommitStat {
        long userId;
        long commitCount;
    }

    @Test
    void 사용자별_commit_top10_랭킹이_정렬되어있어야_한다() {
        List<UserCommitStat> list = jdbcTemplate.query(
                """
                SELECT USER_ACCOUNT_ID, COUNT(*) AS cnt
                FROM COMMIT_LOG
                GROUP BY USER_ACCOUNT_ID
                ORDER BY cnt DESC
                LIMIT 10
                """,
                (rs, rowNum) -> {
                    UserCommitStat s = new UserCommitStat();
                    s.userId = rs.getLong("USER_ACCOUNT_ID");
                    s.commitCount = rs.getLong("cnt");
                    return s;
                }
        );

        assertThat(list).isNotEmpty();
        // 10명 미만일 수도 있어서 사이즈만 확인
        assertThat(list.size()).isBetween(1, 10);

        for (int i = 1; i < list.size(); i++) {
            assertThat(list.get(i - 1).commitCount)
                    .as("rank %d vs %d", i - 1, i)
                    .isGreaterThanOrEqualTo(list.get(i).commitCount);
        }
    }

    @Test
    void 전체_commit_수와_repo별_commit합이_일치해야_한다() {
        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM COMMIT_LOG",
                Long.class
        );

        Long sumByRepo = jdbcTemplate.queryForObject(
                """
                SELECT SUM(cnt) FROM (
                    SELECT GITHUB_REPO_ID, COUNT(*) AS cnt
                    FROM COMMIT_LOG
                    GROUP BY GITHUB_REPO_ID
                )
                """,
                Long.class
        );

        assertThat(total).isEqualTo(sumByRepo);
    }

    @Test
    void negative_commit_비율은_0에서_1_사이여야_한다() {
        Long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM COMMIT_ANALYSIS",
                Long.class
        );
        Long negative = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM COMMIT_ANALYSIS WHERE SENTIMENT = 'NEGATIVE'",
                Long.class
        );

        assertThat(total).isNotNull();
        assertThat(total).isGreaterThan(0L);

        double ratio = negative * 1.0 / total;
        System.out.println("NEGATIVE ratio = " + ratio);

        assertThat(ratio).isBetween(0.0, 1.0);
    }
}