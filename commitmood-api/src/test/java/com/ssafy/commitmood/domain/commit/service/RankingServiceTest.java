package com.ssafy.commitmood.domain.commit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ssafy.commitmood.domain.commit.common.RankingOptionsEnum;
import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.UserRankingDetailResponse;
import com.ssafy.commitmood.domain.commit.repository.RankingRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("RankingService 테스트")
class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository;

    @InjectMocks
    private RankingService rankingService;

    @Test
    @DisplayName("커밋 수 기준 랭킹 조회")
    void getRankingByCommitCount() {
        int page = 1;
        int perPage = 10;

        List<UserRankingDto> mockRankings = List.of(
                new UserRankingDto(
                        1L,
                        "user1",
                        "https://avatar1.png",
                        100L,
                        10L,
                        5L,
                        BigDecimal.valueOf(0.75),
                        LocalDateTime.now()
                ),
                new UserRankingDto(
                        2L,
                        "user2",
                        "https://avatar2.png",
                        90L,
                        8L,
                        3L,
                        BigDecimal.valueOf(0.80),
                        LocalDateTime.now()
                )
        );
        when(rankingRepository.findRankingByCommitCount(perPage, 0)).thenReturn(mockRankings);

        RankingListResponse response = rankingService.getRankingList(RankingOptionsEnum.commit_count, page, perPage);

        assertThat(response.rankings()).hasSize(2);
        assertThat(response.page()).isEqualTo(page);
        assertThat(response.perPage()).isEqualTo(perPage);
        verify(rankingRepository).findRankingByCommitCount(perPage, 0);
    }

    @Test
    @DisplayName("플래그 수 기준 랭킹 조회")
    void getRankingByFlaggedCount() {
        int page = 2;
        int perPage = 5;

        List<UserRankingDto> mockRankings = List.of(
                new UserRankingDto(
                        1L,
                        "user1",
                        "https://avatar1.png",
                        50L,
                        20L,
                        10L,
                        BigDecimal.valueOf(0.60),
                        LocalDateTime.now()
                )
        );
        when(rankingRepository.findRankingByFlaggedCount(perPage, 5)).thenReturn(mockRankings);

        RankingListResponse response = rankingService.getRankingList(RankingOptionsEnum.flagged_count, page, perPage);

        assertThat(response.rankings()).hasSize(1);
        verify(rankingRepository).findRankingByFlaggedCount(perPage, 5);
    }

    @Test
    @DisplayName("욕설 수 기준 랭킹 조회")
    void getRankingBySwearCount() {
        int page = 1;
        int perPage = 10;

        when(rankingRepository.findRankingBySwearCount(perPage, 0)).thenReturn(List.of());

        RankingListResponse response = rankingService.getRankingList(RankingOptionsEnum.swear_count, page, perPage);

        assertThat(response.rankings()).isEmpty();
        verify(rankingRepository).findRankingBySwearCount(perPage, 0);
    }

    @Test
    @DisplayName("감정 점수 기준 랭킹 조회")
    void getRankingBySentimentScore() {
        int page = 1;
        int perPage = 10;

        when(rankingRepository.findRankingBySentimentScore(perPage, 0)).thenReturn(List.of());

        rankingService.getRankingList(RankingOptionsEnum.sentiment_score, page, perPage);

        verify(rankingRepository).findRankingBySentimentScore(perPage, 0);
    }

    @Test
    @DisplayName("최근 활동 기준 랭킹 조회")
    void getRankingByRecentActivity() {
        int page = 1;
        int perPage = 10;

        when(rankingRepository.findRankingByRecentActivity(perPage, 0)).thenReturn(List.of());

        rankingService.getRankingList(RankingOptionsEnum.recent, page, perPage);

        verify(rankingRepository).findRankingByRecentActivity(perPage, 0);
    }

    @Test
    @DisplayName("사용자 저장소별 통계 조회")
    void getUserRankingDetailRepo() {
        Long userAccountId = 1L;
        int page = 1;
        int perPage = 10;

        List<UserRepoStatsDto> mockStats = List.of(
                new UserRepoStatsDto(
                        100L,
                        "repo1",
                        "owner/repo1",
                        50L,
                        1000L,
                        500L,
                        LocalDateTime.now()
                ),
                new UserRepoStatsDto(
                        200L,
                        "repo2",
                        "owner/repo2",
                        30L,
                        800L,
                        300L,
                        LocalDateTime.now()
                )
        );
        when(rankingRepository.findUserRepoStats(userAccountId, 10, 0)).thenReturn(mockStats);

        UserRankingDetailResponse response = rankingService.getUserRankingDetail(
                userAccountId, UserRankingDetailOptions.repo, page, perPage
        );

        assertThat(response.detailType()).isEqualTo(UserRankingDetailOptions.repo);
        assertThat(response.detailData()).isInstanceOf(List.class);
        verify(rankingRepository).findUserRepoStats(userAccountId, 10, 0);
    }

    @Test
    @DisplayName("사용자 플래그 통계 조회")
    void getUserRankingDetailFlagged() {
        Long userAccountId = 1L;
        int page = 1;
        int perPage = 10;

        List<UserFlaggedStatsDto> mockStats = List.of(
                new UserFlaggedStatsDto("token1", "SWEAR", 15L, 30L),
                new UserFlaggedStatsDto("token2", "NEGATIVE", 10L, 20L)
        );
        when(rankingRepository.findUserFlaggedStats(userAccountId, 10, 0)).thenReturn(mockStats);

        UserRankingDetailResponse response = rankingService.getUserRankingDetail(
                userAccountId, UserRankingDetailOptions.flagged, page, perPage
        );

        assertThat(response.detailType()).isEqualTo(UserRankingDetailOptions.flagged);
        verify(rankingRepository).findUserFlaggedStats(userAccountId, 10, 0);
    }

    @Test
    @DisplayName("사용자 감정 통계 조회")
    void getUserRankingDetailSentiment() {
        Long userAccountId = 1L;
        int page = 1;
        int perPage = 10;

        UserSentimentStatsDto mockStats = new UserSentimentStatsDto(
                50L, 30L, 20L,
                BigDecimal.valueOf(0.75),
                BigDecimal.valueOf(0.95),
                BigDecimal.valueOf(0.10)
        );
        when(rankingRepository.findUserSentimentStats(userAccountId)).thenReturn(mockStats);

        UserRankingDetailResponse response = rankingService.getUserRankingDetail(
                userAccountId, UserRankingDetailOptions.sentiment, page, perPage
        );

        assertThat(response.detailType()).isEqualTo(UserRankingDetailOptions.sentiment);
        assertThat(response.detailData()).isInstanceOf(UserSentimentStatsDto.class);
        verify(rankingRepository).findUserSentimentStats(userAccountId);
    }

    @Test
    @DisplayName("잘못된 detail 옵션 예외 발생")
    void getUserRankingDetailInvalidOption() {
        Long userAccountId = 1L;
        int page = 1;
        int perPage = 10;

        assertThatThrownBy(() ->
                rankingService.getUserRankingDetail(userAccountId, null, page, perPage)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("페이징 offset 계산 검증")
    void paginationOffsetCalculation() {
        int page = 3;
        int perPage = 20;

        when(rankingRepository.findRankingByCommitCount(20, 40)).thenReturn(List.of());

        rankingService.getRankingList(RankingOptionsEnum.commit_count, page, perPage);

        verify(rankingRepository).findRankingByCommitCount(20, 40);
    }
}