package com.ssafy.commitmood.domain.commit.mapper;

import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("RankingMapper 테스트")
@MapperScan(basePackages = {
        "com.ssafy.commitmood.domain.commit.mapper",
        "com.ssafy.commitmood.domain.user.mapper",
})
@ActiveProfiles("test")
class RankingMapperTest {

    @Autowired
    private RankingMapper rankingMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private GithubRepoInsertMapper githubRepoInsertMapper;

    @Autowired
    private CommitLogMapper commitLogMapper;

    @Autowired
    private CommitAnalysisMapper commitAnalysisMapper;

    @Autowired
    private FlaggedTokenMapper flaggedTokenMapper;

    @Test
    @DisplayName("커밋 수 기준으로 사용자 랭킹을 조회한다")
    void findRankingByCommitCount() {
        // given
        UserAccount user1 = createAndSaveUser(11111L, "user1");
        UserAccount user2 = createAndSaveUser(22222L, "user2");
        UserAccount user3 = createAndSaveUser(33333L, "user3");

        GithubRepo repo1 = createAndSaveRepo(user1.getId(), 67890L, "repo1");
        GithubRepo repo2 = createAndSaveRepo(user2.getId(), 67891L, "repo2");
        GithubRepo repo3 = createAndSaveRepo(user3.getId(), 67892L, "repo3");

        // user1: 5개 커밋
        for (int i = 0; i < 5; i++) {
            createAndSaveCommit(repo1.getId(), user1.getId(), "user1sha" + i);
        }
        // user2: 3개 커밋
        for (int i = 0; i < 3; i++) {
            createAndSaveCommit(repo2.getId(), user2.getId(), "user2sha" + i);
        }
        // user3: 1개 커밋
        createAndSaveCommit(repo3.getId(), user3.getId(), "user3sha0");

        // when
        List<UserRankingDto> result = rankingMapper.findRankingByCommitCount(10, 0);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getGithubLogin()).isEqualTo("user1");
        assertThat(result.get(0).getTotalCommits()).isEqualTo(5L);
        assertThat(result.get(1).getGithubLogin()).isEqualTo("user2");
        assertThat(result.get(1).getTotalCommits()).isEqualTo(3L);
        assertThat(result.get(2).getGithubLogin()).isEqualTo("user3");
        assertThat(result.get(2).getTotalCommits()).isEqualTo(1L);
    }

    @Test
    @DisplayName("플래그 토큰 수 기준으로 사용자 랭킹을 조회한다")
    void findRankingByFlaggedCount() {
        // given
        UserAccount user1 = createAndSaveUser(11111L, "user1");
        UserAccount user2 = createAndSaveUser(22222L, "user2");

        GithubRepo repo1 = createAndSaveRepo(user1.getId(), 67890L, "repo1");
        GithubRepo repo2 = createAndSaveRepo(user2.getId(), 67891L, "repo2");

        CommitLog commit1 = createAndSaveCommit(repo1.getId(), user1.getId(), "sha1");
        CommitLog commit2 = createAndSaveCommit(repo2.getId(), user2.getId(), "sha2");

        // user1: flagged_count = 10
        createAndSaveAnalysis(commit1.getId(), 10L, 5L);
        // user2: flagged_count = 3
        createAndSaveAnalysis(commit2.getId(), 3L, 1L);

        // when
        List<UserRankingDto> result = rankingMapper.findRankingByFlaggedCount(10, 0);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGithubLogin()).isEqualTo("user1");
        assertThat(result.get(0).getTotalFlagged()).isEqualTo(10L);
        assertThat(result.get(1).getGithubLogin()).isEqualTo("user2");
        assertThat(result.get(1).getTotalFlagged()).isEqualTo(3L);
    }

    @Test
    @DisplayName("욕설 수 기준으로 사용자 랭킹을 조회한다")
    void findRankingBySwearCount() {
        // given
        UserAccount user1 = createAndSaveUser(11111L, "user1");
        UserAccount user2 = createAndSaveUser(22222L, "user2");

        GithubRepo repo1 = createAndSaveRepo(user1.getId(), 67890L, "repo1");
        GithubRepo repo2 = createAndSaveRepo(user2.getId(), 67891L, "repo2");

        CommitLog commit1 = createAndSaveCommit(repo1.getId(), user1.getId(), "sha1");
        CommitLog commit2 = createAndSaveCommit(repo2.getId(), user2.getId(), "sha2");

        // user1: swear_count = 7
        createAndSaveAnalysis(commit1.getId(), 10L, 7L);
        // user2: swear_count = 2
        createAndSaveAnalysis(commit2.getId(), 3L, 2L);

        // when
        List<UserRankingDto> result = rankingMapper.findRankingBySwearCount(10, 0);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTotalSwear()).isEqualTo(7L);
        assertThat(result.get(1).getTotalSwear()).isEqualTo(2L);
    }

    @Test
    @DisplayName("감정 점수 기준으로 사용자 랭킹을 조회한다")
    void findRankingBySentimentScore() {
        // given
        UserAccount user1 = createAndSaveUser(11111L, "user1");
        UserAccount user2 = createAndSaveUser(22222L, "user2");

        GithubRepo repo1 = createAndSaveRepo(user1.getId(), 67890L, "repo1");
        GithubRepo repo2 = createAndSaveRepo(user2.getId(), 67891L, "repo2");

        CommitLog commit1 = createAndSaveCommit(repo1.getId(), user1.getId(), "sha1");
        CommitLog commit2 = createAndSaveCommit(repo2.getId(), user2.getId(), "sha2");

        createAndSaveAnalysisWithSentiment(commit1.getId(), BigDecimal.valueOf(0.8));
        createAndSaveAnalysisWithSentiment(commit2.getId(), BigDecimal.valueOf(0.3));

        // when
        List<UserRankingDto> result = rankingMapper.findRankingBySentimentScore(10, 0);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getAvgSentimentScore()).isEqualByComparingTo(BigDecimal.valueOf(0.8));
        assertThat(result.get(1).getAvgSentimentScore()).isEqualByComparingTo(BigDecimal.valueOf(0.3));
    }

    @Test
    @DisplayName("최근 활동 기준으로 사용자 랭킹을 조회한다")
    void findRankingByRecentActivity() {
        // given
        UserAccount user1 = createAndSaveUser(11111L, "user1");
        UserAccount user2 = createAndSaveUser(22222L, "user2");

        GithubRepo repo1 = createAndSaveRepo(user1.getId(), 67890L, "repo1");
        GithubRepo repo2 = createAndSaveRepo(user2.getId(), 67891L, "repo2");

        LocalDateTime now = LocalDateTime.now();
        createAndSaveCommitWithTime(repo1.getId(), user1.getId(), "sha1", now.minusDays(1));
        createAndSaveCommitWithTime(repo2.getId(), user2.getId(), "sha2", now.minusDays(7));

        // when
        List<UserRankingDto> result = rankingMapper.findRankingByRecentActivity(10, 0);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGithubLogin()).isEqualTo("user1"); // 최근 활동
        assertThat(result.get(1).getGithubLogin()).isEqualTo("user2");
    }

    @Test
    @DisplayName("특정 사용자의 저장소별 통계를 조회한다")
    void findUserRepoStats() {
        // given
        UserAccount user = createAndSaveUser(11111L, "testuser");
        GithubRepo repo1 = createAndSaveRepo(user.getId(), 67890L, "repo1");
        GithubRepo repo2 = createAndSaveRepo(user.getId(), 67891L, "repo2");

        createAndSaveCommit(repo1.getId(), user.getId(), "sha1");
        createAndSaveCommit(repo1.getId(), user.getId(), "sha2");
        createAndSaveCommit(repo2.getId(), user.getId(), "sha3");

        // when
        List<UserRepoStatsDto> result = rankingMapper.findUserRepoStats(user.getId(), 10, 0);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGithubRepoName()).isEqualTo("repo1");
        assertThat(result.get(0).getCommitCount()).isEqualTo(2L);
        assertThat(result.get(1).getGithubRepoName()).isEqualTo("repo2");
        assertThat(result.get(1).getCommitCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 사용자의 플래그 토큰 통계를 조회한다")
    void findUserFlaggedStats() {
        // given
        UserAccount user = createAndSaveUser(11111L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "repo");

        CommitLog commit1 = createAndSaveCommit(repo.getId(), user.getId(), "sha1");
        CommitLog commit2 = createAndSaveCommit(repo.getId(), user.getId(), "sha2");

        createAndSaveFlaggedToken(commit1.getId(), "fuck", FlaggedToken.TokenType.SWEAR, 5L);
        createAndSaveFlaggedToken(commit2.getId(), "fuck", FlaggedToken.TokenType.SWEAR, 5L);
        createAndSaveFlaggedToken(commit1.getId(), "damn", FlaggedToken.TokenType.SWEAR, 3L);

        // when
        List<UserFlaggedStatsDto> result = rankingMapper.findUserFlaggedStats(user.getId(), 10, 0);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getToken()).isEqualTo("fuck");
        assertThat(result.get(0).getOccurrenceCount()).isEqualTo(2L); // 2번 사용
        assertThat(result.get(0).getTotalWeight()).isEqualTo(10L); // 5 + 5
        assertThat(result.get(1).getToken()).isEqualTo("damn");
        assertThat(result.get(1).getOccurrenceCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정 사용자의 감정 분석 통계를 조회한다")
    void findUserSentimentStats() {
        // given
        UserAccount user = createAndSaveUser(11111L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "repo");

        CommitLog commit1 = createAndSaveCommit(repo.getId(), user.getId(), "sha1");
        CommitLog commit2 = createAndSaveCommit(repo.getId(), user.getId(), "sha2");
        CommitLog commit3 = createAndSaveCommit(repo.getId(), user.getId(), "sha3");

        createAndSaveAnalysisWithSentiment(commit1.getId(), BigDecimal.valueOf(0.8), CommitAnalysis.Sentiment.POSITIVE);
        createAndSaveAnalysisWithSentiment(commit2.getId(), BigDecimal.valueOf(-0.5), CommitAnalysis.Sentiment.NEGATIVE);
        createAndSaveAnalysisWithSentiment(commit3.getId(), BigDecimal.valueOf(0.0), CommitAnalysis.Sentiment.NEUTRAL);

        // when
        UserSentimentStatsDto result = rankingMapper.findUserSentimentStats(user.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPositiveCount()).isEqualTo(1L);
        assertThat(result.getNeutralCount()).isEqualTo(1L);
        assertThat(result.getNegativeCount()).isEqualTo(1L);
        assertThat(result.getMaxSentimentScore()).isEqualByComparingTo(BigDecimal.valueOf(0.8));
        assertThat(result.getMinSentimentScore()).isEqualByComparingTo(BigDecimal.valueOf(-0.5));
    }

    @Test
    @DisplayName("페이지네이션을 지원한다")
    void pagination() {
        // given
        for (int i = 0; i < 5; i++) {
            UserAccount user = createAndSaveUser(10000L + i, "user" + i);
            GithubRepo repo = createAndSaveRepo(user.getId(), 60000L + i, "repo" + i);
            createAndSaveCommit(repo.getId(), user.getId(), "sha" + i);
        }

        // when
        List<UserRankingDto> page1 = rankingMapper.findRankingByCommitCount(2, 0);
        List<UserRankingDto> page2 = rankingMapper.findRankingByCommitCount(2, 2);

        // then
        assertThat(page1).hasSize(2);
        assertThat(page2).hasSize(2);
        assertThat(page1.get(0).getGithubLogin()).isNotEqualTo(page2.get(0).getGithubLogin());
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
        return createAndSaveCommitWithTime(githubRepoId, userAccountId, sha, LocalDateTime.now());
    }

    private CommitLog createAndSaveCommitWithTime(Long githubRepoId, Long userAccountId,
                                                  String sha, LocalDateTime committedAt) {
        String fullSha = sha + "0000000000000000000000000000000000000";
        fullSha = fullSha.substring(0, 40);

        CommitLog commit = CommitLog.create(
                githubRepoId,
                userAccountId,
                fullSha,
                committedAt,
                "test commit message",
                "https://github.com/commit/" + fullSha,
                10L,
                5L,
                15L,
                3L
        );
        commitLogMapper.insert(commit);
        return commit;
    }

    private CommitAnalysis createAndSaveAnalysis(Long commitLogId, Long flaggedCount, Long swearCount) {
        CommitAnalysis analysis = CommitAnalysis.create(
                commitLogId,
                flaggedCount,
                swearCount,
                0L,
                0L,
                CommitAnalysis.Sentiment.NEUTRAL,
                BigDecimal.ZERO
        );
        commitAnalysisMapper.insert(analysis);
        return analysis;
    }

    private CommitAnalysis createAndSaveAnalysisWithSentiment(Long commitLogId, BigDecimal sentimentScore) {
        return createAndSaveAnalysisWithSentiment(commitLogId, sentimentScore,
                sentimentScore.compareTo(BigDecimal.ZERO) > 0
                        ? CommitAnalysis.Sentiment.POSITIVE
                        : CommitAnalysis.Sentiment.NEGATIVE);
    }

    private CommitAnalysis createAndSaveAnalysisWithSentiment(Long commitLogId, BigDecimal sentimentScore,
                                                              CommitAnalysis.Sentiment sentiment) {
        CommitAnalysis analysis = CommitAnalysis.create(
                commitLogId,
                0L,
                0L,
                0L,
                0L,
                sentiment,
                sentimentScore
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