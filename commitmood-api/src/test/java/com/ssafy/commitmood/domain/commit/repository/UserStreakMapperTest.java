package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import com.ssafy.commitmood.domain.commit.entity.CommitLog;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@MapperScan(basePackages = {
        "com.ssafy.commitmood.domain.commit.repository",
        "com.ssafy.commitmood.domain.user.mapper",
})
@DisplayName("UserStreakMapper 테스트")
class UserStreakMapperTest {

    @Autowired
    private UserStreakMapper userStreakMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;

    @Autowired
    private GithubRepoInsertMapper githubRepoInsertMapper;

    @Autowired
    private CommitLogMapper commitLogMapper;

    @Test
    @DisplayName("특정 기간 동안 사용자의 일별 커밋 수를 조회한다")
    void findDailyCommitCounts() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "test-repo");

        LocalDate today = LocalDate.now();
        createAndSaveCommit(repo.getId(), user.getId(), "sha1", today.atTime(10, 0));
        createAndSaveCommit(repo.getId(), user.getId(), "sha2", today.atTime(14, 0));
        createAndSaveCommit(repo.getId(), user.getId(), "sha3", today.minusDays(1).atTime(10, 0));
        createAndSaveCommit(repo.getId(), user.getId(), "sha4", today.minusDays(2).atTime(10, 0));

        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        // when
        List<DailyCommitCountDto> result = userStreakMapper.findDailyCommitCounts(
                user.getId(), startDate, endDate
        );

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(2).getCommitDate()).isEqualTo(today);
        assertThat(result.get(2).getCommitCount()).isEqualTo(2L); // 오늘 2개
        assertThat(result.get(1).getCommitDate()).isEqualTo(today.minusDays(1));
        assertThat(result.get(1).getCommitCount()).isEqualTo(1L);
        assertThat(result.get(0).getCommitDate()).isEqualTo(today.minusDays(2));
        assertThat(result.get(0).getCommitCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("커밋이 없는 기간은 결과에 포함되지 않는다")
    void findDailyCommitCounts_NoCommits() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "test-repo");

        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        // when
        List<DailyCommitCountDto> result = userStreakMapper.findDailyCommitCounts(
                user.getId(), startDate, endDate
        );

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("특정 기간의 총 커밋 일수를 조회한다")
    void countTotalCommitDays() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo = createAndSaveRepo(user.getId(), 67890L, "test-repo");

        LocalDate today = LocalDate.now();
        createAndSaveCommit(repo.getId(), user.getId(), "sha1", today.atTime(10, 0));
        createAndSaveCommit(repo.getId(), user.getId(), "sha2", today.atTime(14, 0)); // 같은 날
        createAndSaveCommit(repo.getId(), user.getId(), "sha3", today.minusDays(1).atTime(10, 0));
        createAndSaveCommit(repo.getId(), user.getId(), "sha4", today.minusDays(2).atTime(10, 0));
        createAndSaveCommit(repo.getId(), user.getId(), "sha5", today.minusDays(10).atTime(10, 0)); // 범위 밖

        LocalDate startDate = today.minusDays(7);
        LocalDate endDate = today;

        // when
        Integer result = userStreakMapper.countTotalCommitDays(user.getId(), startDate, endDate);

        // then
        assertThat(result).isEqualTo(3); // 3일 동안 커밋
    }

    @Test
    @DisplayName("커밋이 없으면 총 커밋 일수는 0이다")
    void countTotalCommitDays_Zero() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        // when
        Integer result = userStreakMapper.countTotalCommitDays(user.getId(), startDate, endDate);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("여러 저장소의 커밋을 모두 포함하여 집계한다")
    void findDailyCommitCounts_MultipleRepos() {
        // given
        UserAccount user = createAndSaveUser(12345L, "testuser");
        GithubRepo repo1 = createAndSaveRepo(user.getId(), 67890L, "test-repo1");
        GithubRepo repo2 = createAndSaveRepo(user.getId(), 67891L, "test-repo2");

        LocalDate today = LocalDate.now();
        createAndSaveCommit(repo1.getId(), user.getId(), "sha1", today.atTime(10, 0));
        createAndSaveCommit(repo2.getId(), user.getId(), "sha2", today.atTime(14, 0));

        LocalDate startDate = today.minusDays(1);
        LocalDate endDate = today;

        // when
        List<DailyCommitCountDto> result = userStreakMapper.findDailyCommitCounts(
                user.getId(), startDate, endDate
        );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCommitCount()).isEqualTo(2L); // 두 저장소 합산
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

    private CommitLog createAndSaveCommit(Long githubRepoId, Long userAccountId,
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
}