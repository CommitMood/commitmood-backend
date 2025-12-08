// CommitAnalysisQueryServiceTest.java
package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis.Sentiment;
import com.ssafy.commitmood.domain.commit.repository.CommitAnalysisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommitAnalysisQueryServiceTest {

    @Mock
    private CommitAnalysisRepository commitAnalysisRepository;

    @InjectMocks
    private CommitAnalysisQueryService commitAnalysisQueryService;

    @Test
    @DisplayName("commitLogId로 커밋 분석 정보를 조회한다")
    void getAnalysisByCommitLogId_Success() {
        // given
        Long commitLogId = 1L;
        CommitAnalysis mockAnalysis = CommitAnalysis.create(
                commitLogId,
                5L,
                2L,
                3L,
                1L,
                Sentiment.POSITIVE,
                BigDecimal.valueOf(0.75)
        );

        given(commitAnalysisRepository.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(mockAnalysis));

        // when
        CommitAnalysisResponse response = commitAnalysisQueryService
                .getAnalysisByCommitLogId(commitLogId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.commitLogId()).isEqualTo(commitLogId);
        assertThat(response.flaggedCount()).isEqualTo(5L);
        assertThat(response.swearCount()).isEqualTo(2L);
        assertThat(response.exclaimCount()).isEqualTo(3L);
        assertThat(response.emojiCount()).isEqualTo(1L);
        assertThat(response.sentiment()).isEqualTo(Sentiment.POSITIVE);
        assertThat(response.sentimentScore()).isEqualByComparingTo(0.75);

        verify(commitAnalysisRepository).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("존재하지 않는 commitLogId로 조회하면 예외가 발생한다")
    void getAnalysisByCommitLogId_NotFound_ThrowsException() {
        // given
        Long nonExistentCommitLogId = 999L;
        given(commitAnalysisRepository.findByCommitLogId(nonExistentCommitLogId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                commitAnalysisQueryService.getAnalysisByCommitLogId(nonExistentCommitLogId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Commit analysis not found for id : " + nonExistentCommitLogId);

        verify(commitAnalysisRepository).findByCommitLogId(nonExistentCommitLogId);
    }

    @Test
    @DisplayName("NEUTRAL 감정의 커밋 분석 정보를 정상적으로 조회한다")
    void getAnalysisByCommitLogId_NeutralSentiment() {
        // given
        Long commitLogId = 2L;
        CommitAnalysis mockAnalysis = CommitAnalysis.create(
                commitLogId,
                3L,
                0L,
                1L,
                0L,
                Sentiment.NEUTRAL,
                BigDecimal.ZERO
        );

        given(commitAnalysisRepository.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(mockAnalysis));

        // when
        CommitAnalysisResponse response = commitAnalysisQueryService
                .getAnalysisByCommitLogId(commitLogId);

        // then
        assertThat(response.sentiment()).isEqualTo(Sentiment.NEUTRAL);
        assertThat(response.sentimentScore()).isEqualByComparingTo(0.0);
        assertThat(response.swearCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("NEGATIVE 감정의 커밋 분석 정보를 정상적으로 조회한다")
    void getAnalysisByCommitLogId_NegativeSentiment() {
        // given
        Long commitLogId = 3L;
        CommitAnalysis mockAnalysis = CommitAnalysis.create(
                commitLogId,
                10L,
                8L,
                5L,
                0L,
                Sentiment.NEGATIVE,
                BigDecimal.valueOf(-0.85)
        );

        given(commitAnalysisRepository.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(mockAnalysis));

        // when
        CommitAnalysisResponse response = commitAnalysisQueryService
                .getAnalysisByCommitLogId(commitLogId);

        // then
        assertThat(response.sentiment()).isEqualTo(Sentiment.NEGATIVE);
        assertThat(response.sentimentScore()).isEqualByComparingTo(-0.85);
        assertThat(response.flaggedCount()).isEqualTo(10L);
        assertThat(response.swearCount()).isEqualTo(8L);
    }
}