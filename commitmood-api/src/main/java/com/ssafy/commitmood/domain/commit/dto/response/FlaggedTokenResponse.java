package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;

public record FlaggedTokenResponse (
        String token,
        String tokenType,
        Long weight
) {
    public static FlaggedTokenResponse from(FlaggedToken flaggedToken) {
        return new FlaggedTokenResponse(
                flaggedToken.getToken(),
                flaggedToken.getTokenType() != null ? flaggedToken.getTokenType().name() : null,
                flaggedToken.getWeight()
        );
    }
}
