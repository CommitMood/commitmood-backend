package com.ssafy.commitmood.testdata;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("testdata")
class DataConsistencyTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void github_repo는_항상_존재하는_user_account를_가리켜야_한다() {
        Long invalidCount = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*) 
                FROM GITHUB_REPO r
                LEFT JOIN USER_ACCOUNT u ON r.USER_ACCOUNT_ID = u.ID
                WHERE u.ID IS NULL
                """,
                Long.class
        );

        assertThat(invalidCount).isZero();
    }

    @Test
    void commit_log의_USER와_REPO는_항상_존재해야_한다() {
        Long invalidUser = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM COMMIT_LOG c
                LEFT JOIN USER_ACCOUNT u ON c.USER_ACCOUNT_ID = u.ID
                WHERE u.ID IS NULL
                """,
                Long.class
        );

        Long invalidRepo = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM COMMIT_LOG c
                LEFT JOIN GITHUB_REPO r ON c.GITHUB_REPO_ID = r.ID
                WHERE r.ID IS NULL
                """,
                Long.class
        );

        assertThat(invalidUser).isZero();
        assertThat(invalidRepo).isZero();
    }

    @Test
    void commit_file_change의_commit_log_id는_항상_존재하는_commit이어야_한다() {
        Long invalidFile = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(*)
                FROM COMMIT_FILE_CHANGE f
                LEFT JOIN COMMIT_LOG c ON f.COMMIT_LOG_ID = c.ID
                WHERE c.ID IS NULL
                """,
                Long.class
        );

        assertThat(invalidFile).isZero();
    }
}