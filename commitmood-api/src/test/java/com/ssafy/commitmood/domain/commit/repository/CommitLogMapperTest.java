package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MapperScan(basePackages = {
        "com.ssafy.commitmood.domain.commit.repository",
})
@ActiveProfiles("test")
@Transactional
class CommitLogMapperTest {

    @Autowired
    private CommitLogMapper commitLogMapper;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @DisplayName("커밋 로그 INSERT 및 조회")
    void insert() {

        /* -----------------------------------------------
         * GIVEN: FK 대상(USER_ACCOUNT, GITHUB_REPO) 구성
         * 스키마에 존재하는 컬럼만 사용해야 함!
         * ----------------------------------------------- */

        // USER_ACCOUNT INSERT
        jdbc.update("""
                    INSERT INTO USER_ACCOUNT (
                        ID, 
                        GITHUB_USER_ID, 
                        GITHUB_LOGIN,
                        GITHUB_EMAIL, 
                        GITHUB_AVATAR_URL,
                        GITHUB_NAME,
                        LAST_SYNCED_AT,
                        CREATED_AT,
                        UPDATED_AT
                    )
                    VALUES (
                        1,
                        12345,
                        'tester',
                        'test@example.com',
                        'http://avatar',
                        '홍길동',
                        CURRENT_TIMESTAMP,
                        CURRENT_TIMESTAMP,
                        CURRENT_TIMESTAMP
                    )
                """);

        // GITHUB_REPO INSERT
        jdbc.update("""
                    INSERT INTO GITHUB_REPO (
                        ID,
                        USER_ACCOUNT_ID,
                        GITHUB_REPO_ID,
                        GITHUB_REPO_NAME,
                        GITHUB_REPO_FULL_NAME,
                        DEFAULT_BRANCH,
                        DESCRIPTION,
                        GITHUB_REPO_URL,
                        IS_PRIVATE,
                        CREATED_AT,
                        UPDATED_AT
                    )
                    VALUES (
                        100,
                        1,
                        9999,
                        'test-repo',
                        'tester/test-repo',
                        'main',
                        'test description',
                        'https://github.com/tester/test-repo',
                        FALSE,
                        CURRENT_TIMESTAMP,
                        CURRENT_TIMESTAMP
                    )
                """);

        /* -----------------------------------------------
         * WHEN: CommitLog 데이터 생성 + INSERT
         * ----------------------------------------------- */
        CommitLog commitLog = CommitLog.create(
                100L,
                1L,
                "a".repeat(40),
                LocalDateTime.now(),
                "test commit",
                "https://example.com",
                10L,
                2L,
                12L,
                1L
        );

        commitLogMapper.insert(commitLog);

        List<CommitLog> logs = commitLogMapper.findByRepoId(100L);

        assertThat(logs).isNotEmpty();

        CommitLog saved = logs.getFirst();

        assertThat(saved.getGithubRepoId()).isEqualTo(100L);
        assertThat(saved.getUserAccountId()).isEqualTo(1L);
        assertThat(saved.getGithubCommitSha()).isEqualTo("a".repeat(40));
        assertThat(saved.getMessage()).isEqualTo("test commit");
        assertThat(saved.getAdditions()).isEqualTo(10L);
        assertThat(saved.getDeletions()).isEqualTo(2L);
        assertThat(saved.getTotalChanges()).isEqualTo(12L);
        assertThat(saved.getFilesChanged()).isEqualTo(1L);
    }
}