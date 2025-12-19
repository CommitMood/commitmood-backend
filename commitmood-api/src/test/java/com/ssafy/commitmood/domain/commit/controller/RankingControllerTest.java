package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.common.RankingOptionsEnum;
import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.UserRankingDetailResponse;
import com.ssafy.commitmood.domain.commit.service.RankingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RankingController 테스트")
class RankingControllerTest {

    @Mock
    private RankingService rankingService;

    @InjectMocks
    private RankingController rankingController;

    @Test
    @DisplayName("전체 랭킹 조회 성공 - 커밋 수 기준")
    void getRankingListByCommitCount() {
        List<UserRankingDto> mockRankings = List.of(
                new UserRankingDto(1L, "user1", "avatar1.png", 100L, 10L, 5L, BigDecimal.valueOf(0.8), LocalDateTime.now()),
                new UserRankingDto(2L, "user2", "avatar2.png", 90L, 8L, 3L, BigDecimal.valueOf(0.75), LocalDateTime.now())
        );
        RankingListResponse mockResponse = RankingListResponse.of(mockRankings, 1, 30);
        when(rankingService.getRankingList(RankingOptionsEnum.commit_count, 1, 30)).thenReturn(mockResponse);

        ResponseEntity<RankingListResponse> response = rankingController.getRankingList(
                RankingOptionsEnum.commit_count, 1, 30
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().rankings()).hasSize(2);
        assertThat(response.getBody().page()).isEqualTo(1);
        assertThat(response.getBody().perPage()).isEqualTo(30);
        verify(rankingService).getRankingList(RankingOptionsEnum.commit_count, 1, 30);
    }

    @Test
    @DisplayName("전체 랭킹 조회 성공 - 플래그 수 기준")
    void getRankingListByFlaggedCount() {
        RankingListResponse mockResponse = RankingListResponse.of(List.of(), 2, 10);
        when(rankingService.getRankingList(RankingOptionsEnum.flagged_count, 2, 10)).thenReturn(mockResponse);

        ResponseEntity<RankingListResponse> response = rankingController.getRankingList(
                RankingOptionsEnum.flagged_count, 2, 10
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().page()).isEqualTo(2);
        assertThat(response.getBody().perPage()).isEqualTo(10);
    }

    @Test
    @DisplayName("전체 랭킹 조회 성공 - 욕설 수 기준")
    void getRankingListBySwearCount() {
        RankingListResponse mockResponse = RankingListResponse.of(List.of(), 1, 30);
        when(rankingService.getRankingList(RankingOptionsEnum.swear_count, 1, 30)).thenReturn(mockResponse);

        ResponseEntity<RankingListResponse> response = rankingController.getRankingList(
                RankingOptionsEnum.swear_count, 1, 30
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("전체 랭킹 조회 성공 - 감정 점수 기준")
    void getRankingListBySentimentScore() {
        RankingListResponse mockResponse = RankingListResponse.of(List.of(), 1, 30);
        when(rankingService.getRankingList(RankingOptionsEnum.sentiment_score, 1, 30)).thenReturn(mockResponse);

        ResponseEntity<RankingListResponse> response = rankingController.getRankingList(
                RankingOptionsEnum.sentiment_score, 1, 30
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("전체 랭킹 조회 성공 - 최근 활동 기준")
    void getRankingListByRecent() {
        RankingListResponse mockResponse = RankingListResponse.of(List.of(), 1, 30);
        when(rankingService.getRankingList(RankingOptionsEnum.recent, 1, 30)).thenReturn(mockResponse);

        ResponseEntity<RankingListResponse> response = rankingController.getRankingList(
                RankingOptionsEnum.recent, 1, 30
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("사용자 랭킹 상세 조회 성공 - repo")
    void getUserRankingDetailRepo() {
        UserRankingDetailResponse mockResponse = UserRankingDetailResponse.ofRepo(List.of(), 1, 30);
        when(rankingService.getUserRankingDetail(1L, UserRankingDetailOptions.repo, 1, 30))
                .thenReturn(mockResponse);

        ResponseEntity<UserRankingDetailResponse> response = rankingController.getUserRankingDetail(
                1L, UserRankingDetailOptions.repo, 1, 30
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().detailType()).isEqualTo(UserRankingDetailOptions.repo);
        verify(rankingService).getUserRankingDetail(1L, UserRankingDetailOptions.repo, 1, 30);
    }

    @Test
    @DisplayName("사용자 랭킹 상세 조회 성공 - flagged")
    void getUserRankingDetailFlagged() {
        UserRankingDetailResponse mockResponse = UserRankingDetailResponse.ofFlagged(List.of(), 2, 10);
        when(rankingService.getUserRankingDetail(1L, UserRankingDetailOptions.flagged, 2, 10))
                .thenReturn(mockResponse);

        ResponseEntity<UserRankingDetailResponse> response = rankingController.getUserRankingDetail(
                1L, UserRankingDetailOptions.flagged, 2, 10
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().detailType()).isEqualTo(UserRankingDetailOptions.flagged);
    }

    @Test
    @DisplayName("사용자 랭킹 상세 조회 성공 - sentiment")
    void getUserRankingDetailSentiment() {
        UserRankingDetailResponse mockResponse = UserRankingDetailResponse.ofSentiment(null, 1, 30);
        when(rankingService.getUserRankingDetail(1L, UserRankingDetailOptions.sentiment, 1, 30))
                .thenReturn(mockResponse);

        ResponseEntity<UserRankingDetailResponse> response = rankingController.getUserRankingDetail(
                1L, UserRankingDetailOptions.sentiment, 1, 30
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().detailType()).isEqualTo(UserRankingDetailOptions.sentiment);
    }

    @Test
    @DisplayName("사용자 랭킹 상세 조회 실패 - null detail")
    void getUserRankingDetailNullDetail() {
        when(rankingService.getUserRankingDetail(anyLong(), isNull(), anyInt(), anyInt()))
                .thenThrow(new IllegalArgumentException("유효하지 않은 상세 타입입니다"));

        assertThatThrownBy(() -> rankingController.getUserRankingDetail(1L, null, 1, 30))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 상세 타입입니다");
    }
}