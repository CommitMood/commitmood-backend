package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.repository.GithubRepoRepositoryImpl;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@Import({CommitLogRepositoryImpl.class, GithubRepoRepositoryImpl.class})
class CommitLogRepositoryTest {

    @Autowired
    CommitLogRepository commitLogRepository;

    @Autowired
    GithubRepoRepositoryImpl repoRepository;

    @Autowired
    UserAccountMapper userMapper;

    private Long insertUser(String login) {
        UserAccount user = UserAccount.create(
                System.nanoTime(),
                login,
                login + "@mail.com",
                "avatar",
                login + "Name"
        );
        userMapper.insert(user);
        return user.getId();
    }

    private Long insertRepo(Long userId, String name) {
        GithubRepo repo = GithubRepo.create(
                userId,
                System.nanoTime(),
                name,
                "devys/" + name,
                "main",
                "desc",
                "url",
                false
        );
        return repoRepository.save(repo).getId();
    }

    private CommitLog insertCommit(Long repoId, Long userId, String sha) {
        CommitLog log = CommitLog.create(
                repoId,
                userId,
                sha,
                LocalDateTime.now(),
                "commit-message",
                null,
                0L, 0L, 0L, null
        );
        commitLogRepository.save(log);
        return log;
    }

    @Test
    @DisplayName("save + findByRepoId 정상 조회")
    void saveAndFind() {
        Long uid = insertUser("user1");
        Long rid = insertRepo(uid, "repo1");

        insertCommit(rid, uid, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        List<CommitLog> logs = commitLogRepository.findByRepoId(rid);

        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getGithubRepoId()).isEqualTo(rid);
    }

    @Test
    @DisplayName("여러 commit insert 시 정상 조회")
    void multiInsert() {
        Long uid = insertUser("user2");
        Long rid = insertRepo(uid, "repo2");

        insertCommit(rid, uid, "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
        insertCommit(rid, uid, "cccccccccccccccccccccccccccccccccccccccc");

        List<CommitLog> logs = commitLogRepository.findByRepoId(rid);

        assertThat(logs).hasSize(2);
    }

    @Test
    @DisplayName("CommitLog 저장 후 엔티티 값 검증")
    void checkSavedFields() {
        Long uid = insertUser("user3");
        Long rid = insertRepo(uid, "repo3");

        CommitLog saved = insertCommit(
                rid,
                uid,
                "dddddddddddddddddddddddddddddddddddddddd"
        );

        List<CommitLog> logs = commitLogRepository.findByRepoId(rid);

        assertThat(logs).isNotEmpty();
        CommitLog found = logs.getFirst();

        assertThat(found.getGithubCommitSha()).isEqualTo(saved.getGithubCommitSha());
        assertThat(found.getUserAccountId()).isEqualTo(uid);
        assertThat(found.getGithubRepoId()).isEqualTo(rid);
    }
}