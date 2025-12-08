package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper.RankingEntry;
import lombok.Builder;

@Builder
public record RankingListResponse(
        Long userAccountId,
        String githubLogin,
        String githubAvatarUrl,
        Integer commitCount,
        Integer flaggedCount,
        Integer swearCount,
        Double sentimentScore,
        Integer recentCommitCount
) {
    public static RankingListResponse from(RankingEntry entry) {
        return RankingListResponse.builder()
                .userAccountId(entry.userAccountId())
                .githubLogin(entry.githubLogin())
                .githubAvatarUrl(entry.githubAvatarUrl())
                .commitCount(entry.commitCount())
                .flaggedCount(entry.flaggedCount())
                .swearCount(entry.swearCount())
                .sentimentScore(entry.sentimentScore())
                .recentCommitCount(entry.recentCommitCount())
                .build();
    }
}