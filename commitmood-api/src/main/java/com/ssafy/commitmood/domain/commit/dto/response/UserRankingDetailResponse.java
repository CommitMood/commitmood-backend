package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;

import java.util.List;

public record UserRankingDetailResponse(
        UserRankingDetailOptions detailType,
        Object detailData,
        int page,
        int perPage
) {
    public static UserRankingDetailResponse ofRepo(
            List<UserRepoStatsDto> repoStats,
            int page,
            int perPage
    ) {
        return new UserRankingDetailResponse(UserRankingDetailOptions.repo, repoStats, page, perPage);
    }

    public static UserRankingDetailResponse ofFlagged(
            List<UserFlaggedStatsDto> flaggedStats,
            int page,
            int perPage
    ) {
        return new UserRankingDetailResponse(UserRankingDetailOptions.flagged, flaggedStats, page, perPage);
    }

    public static UserRankingDetailResponse ofSentiment(
            UserSentimentStatsDto sentimentStats,
            int page,
            int perPage
    ) {
        return new UserRankingDetailResponse(UserRankingDetailOptions.sentiment, sentimentStats, page, perPage);
    }
}
