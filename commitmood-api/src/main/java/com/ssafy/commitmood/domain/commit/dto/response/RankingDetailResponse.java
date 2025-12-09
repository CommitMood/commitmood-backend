package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper.UserRankingDetail;
import lombok.Builder;

@Builder
public record RankingDetailResponse(
        Long commitLogId,
        String repoFullName,
        String message,
        String committedAt,
        Long flaggedCount,
        Long swearCount,
        Long sentiment,
        Double sentimentScore,
        String githubUrl
) {
    public static RankingDetailResponse from(UserRankingDetail detail) {
        return RankingDetailResponse.builder()
                .commitLogId(detail.commitLogId())
                .repoFullName(detail.repoFullName())
                .message(detail.message())
                .committedAt(detail.committedAt())
                .flaggedCount(detail.flaggedCount())
                .swearCount(detail.swearCount())
                .sentiment(detail.sentiment())
                .sentimentScore(detail.sentimentScore())
                .githubUrl(detail.githubUrl())
                .build();
    }
}
