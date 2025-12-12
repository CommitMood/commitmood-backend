package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.mapper.CommitAnalysisMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommitAnalysisService 테스트")
public class CommitAnalysisServiceTest {

    @Mock
    private CommitAnalysisMapper commitAnalysisMapper;

    @InjectMocks
    private CommitAnalysisService commitAnalysisService;

    @Test
    @DisplayName("커밋 분석 정보를 조회한다.")
    void getCommitAnalysis() {
        Long commitLogId = 1L;
        CommitAnalysis analysis = CommitAnalysis.create(
                commitLogId,
                5L,
                2L,
                3L,
                1L,
                CommitAnalysis.Sentiment.NEGATIVE,
                BigDecimal.valueOf(-0.5)
        );

        given(commitAnalysisMapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.of(analysis));

        CommitAnalysisResponse response = commitAnalysisService.getCommitAnalysis(commitLogId);

        assertThat(response).isNotNull();
        assertThat(response.commitLogId()).isEqualTo(commitLogId);
        assertThat(response.flaggedCount()).isEqualTo(5L);
        assertThat(response.swearCount()).isEqualTo(2L);
        assertThat(response.sentiment()).isEqualTo("NEGATIVE");
        assertThat(response.sentimentScore()).isEqualByComparingTo(BigDecimal.valueOf(-0.5));

        verify(commitAnalysisMapper).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("존재하지 않는 커밋 분석 정보 조회시 예외를 발생시킨다.")
    void getCommitAnalysis_NotFound() {
        Long commitLogId = 999L;
        given(commitAnalysisMapper.findByCommitLogId(commitLogId))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> commitAnalysisService.getCommitAnalysis(commitLogId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("커밋 분석 정보를 찾을 수 없습니다. commitLogId: " + commitLogId);

        verify(commitAnalysisMapper).findByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("플래그된 토큰 목록을 조회한다.")
    void getFlaggedTokens() {
        Long commitLogId = 1L;
        FlaggedToken token1 = FlaggedToken.create(commitLogId, "badword", FlaggedToken.TokenType.SWEAR, 10L);
        FlaggedToken token2 = FlaggedToken.create(commitLogId, "!!!", FlaggedToken.TokenType.EMPHASIS, 5L);
        FlaggedToken token3 = FlaggedToken.create(commitLogId, "😊", FlaggedToken.TokenType.EMOJI, 3L);

        // 그 아래 mapper의 메서드를 모킹해 특정 반환값을 설정
        given(commitAnalysisMapper.findFlaggedTokensByCommitLogId(commitLogId))
                .willReturn(Arrays.asList(token1, token2, token3));

        List<FlaggedTokenResponse> responses = commitAnalysisService.getFlaggedTokens(commitLogId);

        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).token()).isEqualTo("badword");
        assertThat(responses.get(0).tokenType()).isEqualTo("SWEAR");
        assertThat(responses.get(0).weight()).isEqualTo(10L);
        assertThat(responses.get(1).token()).isEqualTo("!!!");
        assertThat(responses.get(1).tokenType()).isEqualTo("EMPHASIS");
        assertThat(responses.get(1).weight()).isEqualTo(5L);
        assertThat(responses.get(2).token()).isEqualTo("😊");
        assertThat(responses.get(2).tokenType()).isEqualTo("EMOJI");
        assertThat(responses.get(2).weight()).isEqualTo(3L);

        verify(commitAnalysisMapper).findFlaggedTokensByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("플래그된 토큰이 없으면 빈 리스트를 반환한다.")
    void getFlaggedTokens_Empty() {
        Long commitLogId = 1L;
        given(commitAnalysisMapper.findFlaggedTokensByCommitLogId(commitLogId))
                .willReturn(Collections.emptyList());

        List<FlaggedTokenResponse> responses = commitAnalysisService.getFlaggedTokens(commitLogId);

        assertThat(responses).isEmpty();
        verify(commitAnalysisMapper).findFlaggedTokensByCommitLogId(commitLogId);
    }
}
