package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenResponse;
import com.ssafy.commitmood.domain.commit.service.CommitAnalysisService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommitAnalysisController 테스트")
class CommitAnalysisControllerTest {

    @Mock
    private CommitAnalysisService commitAnalysisService;

    @InjectMocks
    private CommitAnalysisController commitAnalysisController;

    @Test
    @DisplayName("커밋 분석 정보 조회 성공")
    void getCommitAnalysis() {
        CommitAnalysisResponse mockResponse = new CommitAnalysisResponse(
                1L, 5L, 2L, 3L, 1L,
                "POSITIVE", BigDecimal.valueOf(0.85), LocalDateTime.now()
        );
        when(commitAnalysisService.getCommitAnalysis(anyLong())).thenReturn(mockResponse);

        ResponseEntity<CommitAnalysisResponse> response = commitAnalysisController.getCommitAnalysis(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().commitLogId()).isEqualTo(1L);
        assertThat(response.getBody().flaggedCount()).isEqualTo(5L);
        assertThat(response.getBody().sentiment()).isEqualTo("POSITIVE");
        verify(commitAnalysisService).getCommitAnalysis(1L);
    }

    @Test
    @DisplayName("커밋 분석 정보 조회 실패 - 존재하지 않는 커밋")
    void getCommitAnalysisNotFound() {
        when(commitAnalysisService.getCommitAnalysis(anyLong()))
                .thenThrow(new IllegalArgumentException("커밋 분석 정보를 찾을 수 없습니다"));

        assertThatThrownBy(() -> commitAnalysisController.getCommitAnalysis(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("커밋 분석 정보를 찾을 수 없습니다");
    }

    @Test
    @DisplayName("플래그 토큰 목록 조회 성공")
    void getFlaggedTokens() {
        List<FlaggedTokenResponse> mockResponse = List.of(
                new FlaggedTokenResponse("token1", "SWEAR", 3L),
                new FlaggedTokenResponse("token2", "NEGATIVE", 2L)
        );
        when(commitAnalysisService.getFlaggedTokens(anyLong())).thenReturn(mockResponse);

        ResponseEntity<List<FlaggedTokenResponse>> response = commitAnalysisController.getFlaggedTokens(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).token()).isEqualTo("token1");
        assertThat(response.getBody().get(0).tokenType()).isEqualTo("SWEAR");
        verify(commitAnalysisService).getFlaggedTokens(1L);
    }

    @Test
    @DisplayName("플래그 토큰 목록 조회 - 빈 결과")
    void getFlaggedTokensEmpty() {
        when(commitAnalysisService.getFlaggedTokens(anyLong())).thenReturn(List.of());

        ResponseEntity<List<FlaggedTokenResponse>> response = commitAnalysisController.getFlaggedTokens(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }
}