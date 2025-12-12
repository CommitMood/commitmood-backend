package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.repository.GithubRepoRepository;
import com.ssafy.commitmood.domain.github.repository.GithubRepoRepositoryImpl;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@Import({CommitAnalysisRepositoryImpl.class,
        CommitLogRepositoryImpl.class,
        GithubRepoRepositoryImpl.class})
class CommitAnalysisRepositoryTest {

    @Autowired
    CommitAnalysisRepository analysisRepo;

    @Autowired
    CommitLogRepository logRepo;

    @Autowired
    GithubRepoRepository repoRepo;

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

    private Long insertRepo(Long userId) {
        GithubRepo repo = GithubRepo.create(
                userId,
                System.nanoTime(),
                "repo",
                "devys/repo",
                "main",
                "desc",
                "url",
                false
        );
        return repoRepo.save(repo).getId();
    }

    private Long insertCommitLog(Long repoId, Long userId) {
        CommitLog log = CommitLog.create(
                repoId,
                userId,
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                LocalDateTime.now(),
                "message",
                null,
                0L, 0L, 0L, null
        );
        logRepo.save(log);
        return log.getId();
    }

    @Test
    void saveAndFind() {
        Long uid = insertUser("a");
        Long rid = insertRepo(uid);
        Long lid = insertCommitLog(rid, uid);

        CommitAnalysis analysis = CommitAnalysis.create(
                lid,
                1L, 1L, 1L, 1L,
                CommitAnalysis.Sentiment.NEGATIVE,
                BigDecimal.valueOf(-0.5)
        );

        analysisRepo.save(analysis);

        var found = analysisRepo.findByCommitLogId(lid);
        assertThat(found).isPresent();
    }
}