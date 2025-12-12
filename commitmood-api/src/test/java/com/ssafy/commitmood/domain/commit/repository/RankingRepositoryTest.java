package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@Import({RankingRepositoryImpl.class})
class RankingRepositoryTest {

    @Autowired
    private RankingRepository rankingRepository;

    @Test
    @DisplayName("전체 사용자 랭킹 - 커밋 수 기준 조회")
    void findRankingByCommitCount() {
        List<UserRankingDto> result = rankingRepository.findRankingByCommitCount(10, 0);

        assertThat(result).isNotNull();
        if (!result.isEmpty()) {
            assertThat(result.getFirst().getUserAccountId()).isNotNull();
        }
    }

    @Test
    @DisplayName("전체 사용자 랭킹 - 플래그 토큰 수 기준 조회")
    void findRankingByFlaggedCount() {
        List<UserRankingDto> result = rankingRepository.findRankingByFlaggedCount(10, 0);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("전체 사용자 랭킹 - 욕설 수 기준 조회")
    void findRankingBySwearCount() {
        List<UserRankingDto> result = rankingRepository.findRankingBySwearCount(10, 0);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("전체 사용자 랭킹 - 감정 점수 기준 조회")
    void findRankingBySentimentScore() {
        List<UserRankingDto> result = rankingRepository.findRankingBySentimentScore(10, 0);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("전체 사용자 랭킹 - 최근 활동 기준 조회")
    void findRankingByRecentActivity() {
        List<UserRankingDto> result = rankingRepository.findRankingByRecentActivity(10, 0);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("특정 사용자의 저장소별 통계 조회")
    void findUserRepoStats() {
        List<UserRepoStatsDto> result = rankingRepository.findUserRepoStats(1L, 10, 0);

        assertThat(result).isNotNull();
        if (!result.isEmpty()) {
            assertThat(result.getFirst().getGithubRepoId()).isNotNull();
        }
    }

    @Test
    @DisplayName("특정 사용자의 플래그 토큰 통계 조회")
    void findUserFlaggedStats() {
        List<UserFlaggedStatsDto> result = rankingRepository.findUserFlaggedStats(1L, 10, 0);

        assertThat(result).isNotNull();
        if (!result.isEmpty()) {
            assertThat(result.getFirst().getToken()).isNotNull();
        }
    }

    @Test
    @DisplayName("특정 사용자의 감정 분석 통계 조회")
    void findUserSentimentStats() {
        UserSentimentStatsDto dto = rankingRepository.findUserSentimentStats(1L);

        assertThat(dto).isNotNull();
    }
}