package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import lombok.Builder;

import java.util.List;

@Builder
public record FlaggedTokenListResponse(
        Long commitLogId,
        List<FlaggedTokenResponse> tokens,
        Integer totalCount
) {
    public static FlaggedTokenListResponse from(Long commitLogId, List<FlaggedToken> tokens) {
        List<FlaggedTokenResponse> tokenResponses = tokens.stream()
                .map(FlaggedTokenResponse::from)
                .toList();

        return FlaggedTokenListResponse.builder()
                .commitLogId(commitLogId)
                .tokens(tokenResponses)
                .totalCount(tokenResponses.size())
                .build();
    }
}