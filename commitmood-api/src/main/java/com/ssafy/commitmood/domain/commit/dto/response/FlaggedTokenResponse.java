package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FlaggedTokenResponse(
        Long flaggedTokenId,
        Long commitLogId,
        String token,
        FlaggedToken.TokenType tokenType,
        Long weight,
        LocalDateTime createdAt
) {
    public static FlaggedTokenResponse from(FlaggedToken flaggedToken) {
        return FlaggedTokenResponse.builder()
                .flaggedTokenId(flaggedToken.getId())
                .commitLogId(flaggedToken.getCommitLogId())
                .token(flaggedToken.getToken())
                .tokenType(flaggedToken.getTokenType())
                .weight(flaggedToken.getWeight())
                .createdAt(flaggedToken.getCreatedAt())
                .build();
    }
}
