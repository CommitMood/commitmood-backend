package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import java.math.BigDecimal;
import java.sql.Connection;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@Import(CommitAnalysisRepositoryImpl.class)
class CommitAnalysisRepositoryTest {

    @Autowired
    private CommitAnalysisRepository commitAnalysisRepository;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setup() throws Exception {
        try (Connection conn = dataSource.getConnection()) {

            // USER_ACCOUNT
            conn.prepareStatement("""
                        INSERT INTO USER_ACCOUNT
                        (ID, GITHUB_USER_ID, GITHUB_LOGIN, CREATED_AT, UPDATED_AT)
                        VALUES (1, 100, 'tester', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                    """).executeUpdate();

            // GITHUB_REPO
            conn.prepareStatement("""
                        INSERT INTO GITHUB_REPO
                        (ID, USER_ACCOUNT_ID, GITHUB_REPO_ID, GITHUB_REPO_NAME, GITHUB_REPO_FULL_NAME,
                         DEFAULT_BRANCH, DESCRIPTION, GITHUB_REPO_URL, IS_PRIVATE, LAST_SYNCED_AT,
                         CREATED_AT, UPDATED_AT)
                        VALUES
                        (1, 1, 500, 'repo', 'tester/repo', 'main',
                         NULL, NULL, FALSE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                    """).executeUpdate();

            // COMMIT_LOG
            conn.prepareStatement("""
                        INSERT INTO COMMIT_LOG
                        (ID, GITHUB_REPO_ID, USER_ACCOUNT_ID,
                         GITHUB_COMMIT_SHA, COMMITTED_AT, MESSAGE,
                         CREATED_AT, UPDATED_AT)
                        VALUES
                        (1, 1, 1,
                         'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa',
                         CURRENT_TIMESTAMP, 'test commit',
                         CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
                    """).executeUpdate();
        }
    }

    @Test
    void saveAndFind() {
        CommitAnalysis analysis = CommitAnalysis.create(
                1L, 5L, 2L, 3L, 1L,
                CommitAnalysis.Sentiment.NEGATIVE,
                BigDecimal.valueOf(-0.4)
        );

        commitAnalysisRepository.save(analysis);

        var found = commitAnalysisRepository.findByCommitLogId(1L);
        assertThat(found).isPresent();
    }
}