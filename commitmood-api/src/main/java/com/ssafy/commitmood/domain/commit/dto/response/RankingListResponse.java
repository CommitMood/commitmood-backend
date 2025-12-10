package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;

import java.util.List;

public record RankingListResponse (
        List<UserRankingDto> rankings,
        int page,
        int perPage,
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
