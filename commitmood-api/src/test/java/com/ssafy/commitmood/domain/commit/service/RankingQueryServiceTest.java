package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.repository.RankingRepository;
import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper.RankingEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RankingQueryServiceTest {

    @Mock
    private RankingRepository repository;

    @InjectMocks
    private RankingQueryService service;

    private RankingEntry createMockEntry(Long userId, String login, int commitCount, int flaggedCount) {
        return new RankingEntry(userId, login, "", commitCount, flaggedCount, 0, 0.0, 0);
    }

    @Test
    @DisplayName("커밋 수 기준 랭킹 조회")
    void getRankings_CommitCount() {
        List<RankingEntry> mockEntries = List.of(
                createMockEntry(1L, "user1", 10, 5),
                createMockEntry(2L, "user2", 8, 3)
        );

        given(repository.findRankingByCommitCount(30, 0))
                .willReturn(mockEntries);
        given(repository.countTotalUsers()).willReturn(100);

        PageResponse<RankingListResponse> response = service
                .getRankings("commit_count", 1, 30);

        assertThat(response.content()).hasSize(2);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(30);
        assertThat(response.totalCount()).isEqualTo(100);

        verify(repository).findRankingByCommitCount(30, 0);
        verify(repository).countTotalUsers();
    }

    @Test
    @DisplayName("플래그 토큰 수 기준 랭킹 조회")
    void getRankings_FlaggedCount() {
        
        given(repository.findRankingByFlaggedCount(anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        
        service.getRankings("flagged_count", 1, 30);

        
        verify(repository).findRankingByFlaggedCount(30, 0);
    }

    @Test
    @DisplayName("욕설 수 기준 랭킹 조회")
    void getRankings_SwearCount() {
        given(repository.findRankingBySwearCount(anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        service.getRankings("swear_count", 1, 30);

        verify(repository).findRankingBySwearCount(30, 0);
    }

    @Test
    @DisplayName("감정 점수 기준 랭킹 조회")
    void getRankings_SentimentScore() {
        given(repository.findRankingBySentimentScore(anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        service.getRankings("sentiment_score", 1, 30);

        verify(repository).findRankingBySentimentScore(30, 0);
    }

    @Test
    @DisplayName("최근 활동 기준 랭킹 조회")
    void getRankings_Recent() {
        given(repository.findRankingByRecent(any(LocalDateTime.class), anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        service.getRankings("recent", 1, 30);

        verify(repository).findRankingByRecent(any(LocalDateTime.class), anyInt(), anyInt());
    }

    @Test
    @DisplayName("옵션이 null이면 기본값(commit_count) 적용")
    void getRankings_DefaultOption() {
        given(repository.findRankingByCommitCount(anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        service.getRankings(null, 1, 30);

        verify(repository).findRankingByCommitCount(30, 0);
    }

    @Test
    @DisplayName("알 수 없는 옵션이면 기본값(commit_count) 적용")
    void getRankings_UnknownOption() {
        given(repository.findRankingByCommitCount(anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        service.getRankings("unknown", 1, 30);

        verify(repository).findRankingByCommitCount(30, 0);
    }

    @Test
    @DisplayName("페이징 처리 - 2페이지 조회")
    void getRankings_Paging() {
        given(repository.findRankingByCommitCount(30, 30))
                .willReturn(List.of());
        given(repository.countTotalUsers()).willReturn(100);

        PageResponse<RankingListResponse> response = service
                .getRankings("commit_count", 2, 30);

        assertThat(response.page()).isEqualTo(2);
        verify(repository).findRankingByCommitCount(30, 30);
    }
}
