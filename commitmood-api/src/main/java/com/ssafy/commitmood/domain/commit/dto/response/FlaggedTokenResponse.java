package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "플래그 토큰 응답")
public record FlaggedTokenResponse (
        @Schema(description = "해당하는 토큰 문자열", example = "laughing")
        String token,

        @Schema(description = "토큰 유형", example = "SWEAR", allowableValues = {"SWEAR", "SLANG", "EMOJI", "EMPHASIS", "OTHER"})
        String tokenType,

        @Schema(description = "토큰 가중치", example = "5")
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
