package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenListResponse;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken.TokenType;
import com.ssafy.commitmood.domain.commit.repository.FlaggedTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommitTokenQueryServiceTest {

    @Mock
    private FlaggedTokenRepository repository;

    @InjectMocks
    private CommitTokenQueryService service;

    @Test
    @DisplayName("commitLogId로 플래그 토큰 목록을 조회한다.")
    void getTokensByCommitLogId_Success() {
        Long commitLogId = 1L;
        List<FlaggedToken> mockTokens = List.of(
                FlaggedToken.create(commitLogId, "damn", TokenType.SWEAR, 10L),
                FlaggedToken.create(commitLogId, "happy", TokenType.EMPHASIS, 5L),
                FlaggedToken.create(commitLogId, "😀", TokenType.EMOJI, 5L)
        );

        given(repository.findAllByCommitLogId(commitLogId))
                .willReturn(mockTokens);

        var response = service.getTokensByCommitLogId(commitLogId);

        assertThat(response).isNotNull();
        assertThat(response.commitLogId()).isEqualTo(commitLogId);
        assertThat(response.tokens()).hasSize(3);
        assertThat(response.tokens().get(0).token()).isEqualTo("damn");
        assertThat(response.tokens().get(0).tokenType()).isEqualTo(TokenType.SWEAR);
        assertThat(response.tokens().get(0).weight()).isEqualTo(10);

        verify(repository).findAllByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("토큰이 없는 커밋의 경우 빈 리스트를 반환한다")
    void getTokensByCommitLogId_EmptyList() {
        Long commitLogId = 2L;
        given(repository.findAllByCommitLogId(commitLogId))
                .willReturn(List.of());

        FlaggedTokenListResponse response = service.getTokensByCommitLogId(commitLogId);

        assertThat(response).isNotNull();
        assertThat(response.commitLogId()).isEqualTo(commitLogId);
        assertThat(response.tokens()).isEmpty();
        assertThat(response.totalCount()).isEqualTo(0);

        verify(repository).findAllByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("다양한 TokenType의 토큰들을 조회한다.")
    void getTokensByCommitLogId_VariousTokenTypes() {
        Long commitLogId = 3L;

        List<FlaggedToken> mockTokens = List.of(
                FlaggedToken.create(commitLogId, "damn", TokenType.SWEAR, 10L),
                FlaggedToken.create(commitLogId, "lol", TokenType.SLANG, 7L),
                FlaggedToken.create(commitLogId, "!!!", TokenType.EMPHASIS, 5L),
                FlaggedToken.create(commitLogId, "😀", TokenType.EMOJI, 3L),
                FlaggedToken.create(commitLogId, "??", TokenType.OTHER, 1L)
        );

        given(repository.findAllByCommitLogId(commitLogId))
                .willReturn(mockTokens);

        FlaggedTokenListResponse response = service.getTokensByCommitLogId(commitLogId);

        assertThat(response.tokens()).hasSize(5);
        assertThat(response.tokens()).extracting("tokenType")
                .containsExactly(
                        TokenType.SWEAR,
                        TokenType.SLANG,
                        TokenType.EMPHASIS,
                        TokenType.EMOJI,
                        TokenType.OTHER
                );
    }
}
