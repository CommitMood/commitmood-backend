package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingDetailResponse;
import com.ssafy.commitmood.domain.commit.service.UserRankingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserRankingQueryController {

    private final UserRankingQueryService service;

    /**
     * 유저 각각의 랭킹 조회
     * @param userAccountId 유저 고유 아이디
     * @param detail 조회 상세 기준 (repo, flagged, sentiment)
     * @param page 페이지
     * @param perPage 페이지 당 개수
     * @return 페이지 응답 객체
     */
    @GetMapping("/{userAccountId}/ranking")
    public PageResponse<RankingDetailResponse> getUserRanking(
            @PathVariable Long userAccountId,
            @RequestParam(required = false, defaultValue = "repo") String detail,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10", name = "per_page") int perPage
    ) {
        return service.getUserRanking(userAccountId, detail, page, perPage);
    }
}
