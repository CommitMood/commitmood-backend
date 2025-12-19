package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자 랭킹 목록 응답")
public record RankingListResponse (
        @Schema(description = "사용자 랭킹 목록")
        List<UserRankingDto> rankings,

        @Schema(description = "현재 페이지", example = "1")
        int page,

        @Schema(description = "페이지당 항목 수", example = "30")
        int perPage,

        @Schema(description = "전체 항목 수", example = "30")
        int totalCount
) {
    public static RankingListResponse of(
            List<UserRankingDto> rankings,
            int page,
            int perPage
    ) {
        return new RankingListResponse(rankings, page, perPage, rankings.size());
    }
}
