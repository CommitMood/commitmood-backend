package com.ssafy.commitmood.testdata;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("testdata")
class PerformanceBenchmarkTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        System.out.println("DB 실제 경로 = " + new java.io.File("./data/commitmood-test.mv.db").getAbsolutePath());
    }

    @Test
    void 최근_커밋_100건_조회는_허용_시간_이내여야_한다() {
        long start = System.currentTimeMillis();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                """
                SELECT *
                FROM COMMIT_LOG
                ORDER BY COMMITTED_AT DESC
                LIMIT 100
                """
        );

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("⏱ latest 100 commits: " + elapsed + "ms");

        assertThat(rows).hasSize(100);
        // 기준은 상황 보고 조절
        assertThat(elapsed).isLessThan(500L);
    }

    @Test
    void commit와_file_change_join_200건_조회는_1초를_넘지_않아야_한다() {
        long start = System.currentTimeMillis();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
                """
                SELECT f.ID, f.COMMIT_LOG_ID, f.FILENAME, f.STATUS,
                       c.MESSAGE, c.COMMITTED_AT
                FROM COMMIT_FILE_CHANGE f
                JOIN COMMIT_LOG c ON f.COMMIT_LOG_ID = c.ID
                ORDER BY f.ID
                LIMIT 200
                """
        );

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("⏱ file+commit join 200: " + elapsed + "ms");

        assertThat(rows).isNotEmpty();
        assertThat(elapsed).isLessThan(1000L);
    }
}