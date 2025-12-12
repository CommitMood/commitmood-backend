package com.ssafy.commitmood.domain.commit.mapper;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("CommitAnalysisMapper 테스트")
@MapperScan(basePackages = {
        "com.ssafy.commitmood.domain.commit.mapper",
        "com.ssafy.commitmood.domain.user.mapper",
})
@ActiveProfiles("test")
class CommitAnalysisMapperTest {

    @Autowired
    private CommitAnalysisMapper commitAnalysisMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private GithubRepoInsertMapper githubRepoInsertMapper;

    @Autowired
    private CommitLogMapper commitLogMapper;

    @Autowired
    private FlaggedTokenMapper flaggedTokenMapper;

    @Test
    @DisplayName("특정 커밋의 분석 정보를 조회한다")
    void findByCommitLogId() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "test-repo");
        CommitLog commit = createAndSaveCommit(repo.getId(), user.getId(), "abc1234567890123456789012345678901234567");
        CommitAnalysis analysis = createAndSaveAnalysis(commit.getId());

        // when
        Optional<CommitAnalysis> result = commitAnalysisMapper.findByCommitLogId(commit.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCommitLogId()).isEqualTo(commit.getId());
        assertThat(result.get().getFlaggedCount()).isEqualTo(5L);
        assertThat(result.get().getSwearCount()).isEqualTo(2L);
        assertThat(result.get().getSentiment()).isEqualTo(CommitAnalysis.Sentiment.NEGATIVE);
    }

    @Test
    @DisplayName("존재하지 않는 커밋 ID로 조회시 빈 Optional을 반환한다")
    void findByCommitLogId_NotFound() {
        // when
        Optional<CommitAnalysis> result = commitAnalysisMapper.findByCommitLogId(999999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("특정 커밋의 플래그된 토큰 목록을 조회한다")
    void findFlaggedTokensByCommitLogId() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "test-repo");
        CommitLog commit = createAndSaveCommit(repo.getId(), user.getId(), "abc1234567890123456789012345678901234567");

        createAndSaveFlaggedToken(commit.getId(), "fuck", FlaggedToken.TokenType.SWEAR, 5L);
        createAndSaveFlaggedToken(commit.getId(), "damn", FlaggedToken.TokenType.SWEAR, 3L);
        createAndSaveFlaggedToken(commit.getId(), "🔥", FlaggedToken.TokenType.EMOJI, 1L);

        // when
        List<FlaggedToken> result = commitAnalysisMapper.findFlaggedTokensByCommitLogId(commit.getId());

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getToken()).isEqualTo("fuck"); // weight 높은 순
        assertThat(result.get(0).getWeight()).isEqualTo(5L);
        assertThat(result.get(1).getToken()).isEqualTo("damn");
        assertThat(result.get(2).getTokenType()).isEqualTo(FlaggedToken.TokenType.EMOJI);
    }

    @Test
    @DisplayName("플래그된 토큰이 없는 커밋은 빈 리스트를 반환한다")
    void findFlaggedTokensByCommitLogId_Empty() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "test-repo");
        CommitLog commit = createAndSaveCommit(repo.getId(), user.getId(), "abc1234567890123456789012345678901234567");

        // when
        List<FlaggedToken> result = commitAnalysisMapper.findFlaggedTokensByCommitLogId(commit.getId());

        // then
        assertThat(result).isEmpty();
    }

    // Helper methods
    private UserAccount createAndSaveUser(Long githubUserId, String githubLogin) {
        UserAccount user = UserAccount.create(
                githubUserId,
                githubLogin,
                "test@example.com",
                "https://avatar.url",
                "Test User"
        );
        userAccountMapper.insert(user);
        return user;
    }

    private GithubRepo createAndSaveRepo(Long userAccountId, Long githubRepoId, String repoName) {
        GithubRepo repo = GithubRepo.create(
                userAccountId,
                githubRepoId,
                repoName,
                "owner/" + repoName,
                "main",
                "Test repository",
                "https://github.com/owner/" + repoName,
                false
        );
        githubRepoInsertMapper.insert(repo);
        return repo;
    }

    private CommitLog createAndSaveCommit(Long githubRepoId, Long userAccountId, String sha) {
        CommitLog commit = CommitLog.create(
                githubRepoId,
                userAccountId,
                sha,
                LocalDateTime.now(),
                "test commit message",
                "https://github.com/commit/" + sha,
                10L,
                5L,
                15L,
                3L
        );
        commitLogMapper.insert(commit);
        return commit;
    }

    private CommitAnalysis createAndSaveAnalysis(Long commitLogId) {
        CommitAnalysis analysis = CommitAnalysis.create(
                commitLogId,
                5L,
                2L,
                3L,
                1L,
                CommitAnalysis.Sentiment.NEGATIVE,
                BigDecimal.valueOf(-0.5)
        );
        commitAnalysisMapper.insert(analysis);
        return analysis;
    }

    private FlaggedToken createAndSaveFlaggedToken(Long commitLogId, String token,
                                                   FlaggedToken.TokenType tokenType, Long weight) {
        FlaggedToken flaggedToken = FlaggedToken.create(
                commitLogId,
                token,
                tokenType,
                weight
        );
        flaggedTokenMapper.insert(flaggedToken);
        return flaggedToken;
    }
}