package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingDetailResponse;
import com.ssafy.commitmood.domain.commit.repository.UserRankingRepository;
import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper.UserRankingDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRankingQueryServiceTest {
    @Mock
    private UserRankingRepository repository;
    
    @InjectMocks
    private UserRankingQueryService service;

    private UserRankingDetail createMockDetail(Long commitLogId, String repoName) {
        return new UserRankingDetail(commitLogId, repoName, "test", "2025-12-09T10:00:00", 5L, 2L, 1L, 0.7, "");
    }
        
    
    @Test
    @DisplayName("저장소별 사용자 랭킹 조회")
    void getUserRanking_Repo() {
        // given
        Long userAccountId = 1L;
        List<UserRankingDetail> mockDetails = List.of(
                createMockDetail(1L, "repo1"),
                createMockDetail(2L, "repo2")
        );
    
        given(repository.findUserRankingByRepo(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(mockDetails);
        given(repository.countUserRankingByRepo(userAccountId)).willReturn(10);
    
        // when
        PageResponse<RankingDetailResponse> response = service
                .getUserRanking(userAccountId, "repo", 1, 30);
    
        // then
        assertThat(response.content()).hasSize(2);
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(30);
        assertThat(response.totalCount()).isEqualTo(10);
    
        verify(repository).findUserRankingByRepo(userAccountId, 30, 0);
        verify(repository).countUserRankingByRepo(userAccountId);
    }
    
    @Test
    @DisplayName("플래그별 사용자 랭킹 조회")
    void getUserRanking_Flagged() {
        // given
        Long userAccountId = 1L;
        given(repository.findUserRankingByFlagged(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countUserRankingByFlagged(userAccountId)).willReturn(5);
    
        // when
        service.getUserRanking(userAccountId, "flagged", 1, 30);
    
        // then
        verify(repository).findUserRankingByFlagged(userAccountId, 30, 0);
        verify(repository).countUserRankingByFlagged(userAccountId);
    }
    
    @Test
    @DisplayName("감정별 사용자 랭킹 조회")
    void getUserRanking_Sentiment() {
        // given
        Long userAccountId = 1L;
        given(repository.findUserRankingBySentiment(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countUserRankingBySentiment(userAccountId)).willReturn(8);
    
        // when
        service.getUserRanking(userAccountId, "sentiment", 1, 30);
    
        // then
        verify(repository).findUserRankingBySentiment(userAccountId, 30, 0);
        verify(repository).countUserRankingBySentiment(userAccountId);
    }
    
    @Test
    @DisplayName("detail이 null이면 기본값(repo) 적용")
    void getUserRanking_DefaultDetail() {
        // given
        Long userAccountId = 1L;
        given(repository.findUserRankingByRepo(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countUserRankingByRepo(userAccountId)).willReturn(0);
    
        // when
        service.getUserRanking(userAccountId, null, 1, 30);
    
        // then
        verify(repository).findUserRankingByRepo(userAccountId, 30, 0);
    }
    
    @Test
    @DisplayName("알 수 없는 detail이면 기본값(repo) 적용")
    void getUserRanking_UnknownDetail() {
        // given
        Long userAccountId = 1L;
        given(repository.findUserRankingByRepo(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countUserRankingByRepo(userAccountId)).willReturn(0);
    
        // when
        service.getUserRanking(userAccountId, "unknown", 1, 30);
    
        // then
        verify(repository).findUserRankingByRepo(userAccountId, 30, 0);
    }
    
    @Test
    @DisplayName("페이징 처리 - 2페이지 조회")
    void getUserRanking_Paging() {
        // given
        Long userAccountId = 1L;
        given(repository.findUserRankingByRepo(userAccountId, 30, 30))
                .willReturn(List.of());
        given(repository.countUserRankingByRepo(userAccountId)).willReturn(100);
    
        // when
        PageResponse<RankingDetailResponse> response = service
                .getUserRanking(userAccountId, "repo", 2, 30);
    
        // then
        assertThat(response.page()).isEqualTo(2);
        verify(repository).findUserRankingByRepo(userAccountId, 30, 30);
    }
    
    @Test
    @DisplayName("빈 결과 처리")
    void getUserRanking_EmptyResult() {
        // given
        Long userAccountId = 1L;
        given(repository.findUserRankingByRepo(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());
        given(repository.countUserRankingByRepo(userAccountId)).willReturn(0);
    
        // when
        PageResponse<RankingDetailResponse> response = service
                .getUserRanking(userAccountId, "repo", 1, 30);
    
        // then
        assertThat(response.content()).isEmpty();
        assertThat(response.totalCount()).isEqualTo(0);
    }
}