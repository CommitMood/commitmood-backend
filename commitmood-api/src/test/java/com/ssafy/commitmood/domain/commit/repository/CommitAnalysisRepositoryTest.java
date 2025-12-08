package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis.Sentiment;
import com.ssafy.commitmood.domain.commit.repository.mapper.CommitAnalysisMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommitAnalysisRepositoryTest {

    @Mock
    private CommitAnalysisMapper mapper;

    @InjectMocks
    private CommitAnalysisRepository repository;

    @Test
    @DisplayName("commitLogId로 커밋 분석 정보를 조회한다")
    void findByCommitLogId_Success() {
        // given
        Long commitLogId = 1L;
        CommitAnalysis mockAnalysis = CommitAnalysis.create(
                commitLogId,
                5L, 2L, 3L, 1L,
                Sentiment.POSITIVE,
                BigDecimal.valueOf(0.75)
        );

        given(mapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(mockAnalysis));

        // when
        Optional<CommitAnalysis> result = repository.findByCommitLogId(commitLogId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCommitLogId()).isEqualTo(commitLogId);
        assertThat(result.get().getSentiment()).isEqualTo(Sentiment.POSITIVE);
        verify(mapper).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("존재하지 않는 commitLogId 조회 시 Optional.empty 반환")
    void findByCommitLogId_NotFound() {
        // given
        Long commitLogId = 999L;
        given(mapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.empty());

        // when
        Optional<CommitAnalysis> result = repository.findByCommitLogId(commitLogId);

        // then
        assertThat(result).isEmpty();
        verify(mapper).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("NEGATIVE 감정의 커밋 분석 정보를 조회한다")
    void findByCommitLogId_NegativeSentiment() {
        // given
        Long commitLogId = 2L;
        CommitAnalysis mockAnalysis = CommitAnalysis.create(
                commitLogId,
                10L, 8L, 5L, 0L,
                Sentiment.NEGATIVE,
                BigDecimal.valueOf(-0.85)
        );

        given(mapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(mockAnalysis));

        // when
        Optional<CommitAnalysis> result = repository.findByCommitLogId(commitLogId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getSentiment()).isEqualTo(Sentiment.NEGATIVE);
        assertThat(result.get().getSentimentScore()).isEqualByComparingTo(BigDecimal.valueOf(-0.85));
        verify(mapper).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("NEUTRAL 감정의 커밋 분석 정보를 조회한다")
    void findByCommitLogId_NeutralSentiment() {
        // given
        Long commitLogId = 3L;
        CommitAnalysis mockAnalysis = CommitAnalysis.create(
                commitLogId,
                3L, 0L, 1L, 0L,
                Sentiment.NEUTRAL,
                BigDecimal.ZERO
        );

        given(mapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(mockAnalysis));

        // when
        Optional<CommitAnalysis> result = repository.findByCommitLogId(commitLogId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getSentiment()).isEqualTo(Sentiment.NEUTRAL);
        assertThat(result.get().getSentimentScore()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.get().getSwearCount()).isEqualTo(0L);
        verify(mapper).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("mapper 호출이 정확히 한 번 발생하는지 검증")
    void findByCommitLogId_VerifyMapperCall() {
        // given
        Long commitLogId = 4L;
        given(mapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.empty());

        // when
        repository.findByCommitLogId(commitLogId);

        // then
        verify(mapper).findByCommitLogId(commitLogId);
    }
}