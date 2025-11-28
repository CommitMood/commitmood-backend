package com.ssafy.commitmood.domain.commit.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = {"commitLogId", "token", "tokenType"})
public class FlaggedToken extends BaseTimeEntity {

    private Long id;
    private Long commitLogId;
    private String token;
    private TokenType tokenType;
    private Long weight;

    private FlaggedToken(
            Long commitLogId,
            String token,
            TokenType tokenType,
            Long weight
    ) {
        this.commitLogId = commitLogId;
        this.token = token;
        this.tokenType = tokenType != null ? tokenType : TokenType.OTHER;
        this.weight = weight != null ? weight : 1L;
    }

    public static FlaggedToken create(
            Long commitLogId,
            String token,
            TokenType tokenType,
            Long weight
    ) {
        validate(commitLogId, token);
        return new FlaggedToken(
                commitLogId,
                token,
                tokenType,
                weight
        );
    }

    public void updateWeight(Long weight) {
        if (weight != null && weight > 0) {
            this.weight = weight;
        }
    }

    public void updateTokenType(TokenType tokenType) {
        if (tokenType != null) {
            this.tokenType = tokenType;
        }
    }

    private static void validate(Long commitLogId, String token) {
        if (commitLogId == null) {
            throw new IllegalArgumentException("Commit Log ID는 필수입니다.");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token은 비어 있을 수 없습니다.");
        }
    }

    public enum TokenType {
        SWEAR, SLANG, EMOJI, EMPHASIS, OTHER
    }
}