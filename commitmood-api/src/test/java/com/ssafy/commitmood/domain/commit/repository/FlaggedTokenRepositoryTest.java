package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.repository.mapper.FlaggedTokenMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken.TokenType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FlaggedTokenRepositoryTest {

    @Mock
    private FlaggedTokenMapper mapper;

    @InjectMocks
    private FlaggedTokenRepository repository;

    @Test
    @DisplayName("commitLogID로 플래그 토큰 목록을 조회한다.")
    void findAllByCommitLogId_Success() {
        Long commitLogId = 1L;
        String token1 = "damn";
        String token2 = "happy";

        List<FlaggedToken> mockTokens = List.of(
            FlaggedToken.create(commitLogId, token1, TokenType.SWEAR, 10L),
            FlaggedToken.create(commitLogId, token2, TokenType.EMPHASIS, 5L)
        );

        // BDD 방식 given 설정 -> commitLogId로 조회 시 mockTokens 반환
        given(mapper.findAllByCommitLogId(commitLogId))
                .willReturn(mockTokens);

        List<FlaggedToken> result = repository.findAllByCommitLogId(commitLogId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getToken()).isEqualTo(token1);
        assertThat(result.get(1).getToken()).isEqualTo(token2);
        verify(mapper).findAllByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("존재하지 않는 commitLogId 조회 시 빈 리스트 반환한다.")
    void findAllByCommitLogId_NotFound() {
        Long commitLogId = 999L;
        given(mapper.findAllByCommitLogId(commitLogId))
                .willReturn(List.of());

        var result = repository.findAllByCommitLogId(commitLogId);

        assertThat(result).isEmpty();
        verify(mapper).findAllByCommitLogId(commitLogId);
    }

    @Test
    @DisplayName("다양한 TokenType의 토큰들을 조회한다.")
    void findAllByCommitLogId_VariousTokenTypes() {
        Long commitLogId = 2L;

        List<FlaggedToken> mockTokens = List.of(
                FlaggedToken.create(commitLogId, "damn", TokenType.SWEAR, 10L),
                FlaggedToken.create(commitLogId, "lol", TokenType.SLANG, 7L),
                FlaggedToken.create(commitLogId, "!!!", TokenType.EMPHASIS, 5L),
                FlaggedToken.create(commitLogId, "😀", TokenType.EMOJI, 3L)
        );

        given(mapper.findAllByCommitLogId(commitLogId))
                .willReturn(mockTokens);

        List<FlaggedToken> result = repository.findAllByCommitLogId(commitLogId);

        assertThat(result).hasSize(4);
        assertThat(result).extracting("tokenType")
                .containsExactly(TokenType.SWEAR, TokenType.SLANG, TokenType.EMPHASIS, TokenType.EMOJI);
        verify(mapper).findAllByCommitLogId(commitLogId);
    }
}
