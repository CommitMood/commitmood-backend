package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자 랭킹 상세 응답")
public record UserRankingDetailResponse(
        @Schema(description = "상세 정보 유형", example = "repo", allowableValues = {"repo", "flagged", "sentiment"})
        UserRankingDetailOptions detailType,

        @Schema(description = "상세 데이터 (타입에 따라 다름)")
        Object detailData,

        @Schema(description = "현재 페이지", example = "1")
        int page,

        @Schema(description = "페이지당 항목 수", example = "30")
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
