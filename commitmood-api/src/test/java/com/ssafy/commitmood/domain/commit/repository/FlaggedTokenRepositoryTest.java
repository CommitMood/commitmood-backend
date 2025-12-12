package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.repository.GithubRepoRepositoryImpl;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@Import({
        FlaggedTokenRepositoryImpl.class,
        CommitLogRepositoryImpl.class,
        GithubRepoRepositoryImpl.class
})
class FlaggedTokenRepositoryTest {

    @Autowired
    FlaggedTokenRepository repository;

    @Autowired
    UserAccountMapper userMapper;

    @Autowired
    GithubRepoRepositoryImpl repoRepository;

    @Autowired
    CommitLogRepositoryImpl commitLogRepository;

    private Long insertUser() {
        UserAccount u = UserAccount.create(
                System.nanoTime(),
                "tokenUser",
                "token@mail.com",
                "avatar",
                "name"
        );
        userMapper.insert(u);
        return u.getId();
    }

    private Long insertRepo(Long userId) {
        GithubRepo repo = GithubRepo.create(
                userId,
                System.nanoTime(),
                "repo",
                "user/repo",
                "main",
                "desc",
                "url",
                false
        );
        return repoRepository.save(repo).getId();
    }

    private Long insertCommit(Long repoId, Long userId) {
        CommitLog log = CommitLog.create(
                repoId,
                userId,
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                LocalDateTime.now(),
                "message",
                null,
                0L, 0L, 0L, null
        );
        commitLogRepository.save(log);
        return log.getId();
    }

    @Test
    @DisplayName("save() 정상 저장")
    void save() {
        Long userId = insertUser();
        Long repoId = insertRepo(userId);
        Long commitId = insertCommit(repoId, userId);

        FlaggedToken token = FlaggedToken.create(
                commitId,
                "bad",
                FlaggedToken.TokenType.SWEAR,
                5L
        );

        FlaggedToken saved = repository.save(token);

        assertThat(saved.getCommitLogId()).isEqualTo(commitId);
        assertThat(saved.getToken()).isEqualTo("bad");
    }
}