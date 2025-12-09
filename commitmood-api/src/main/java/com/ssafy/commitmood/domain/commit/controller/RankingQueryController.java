package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.service.RankingQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("rankings")
@RequiredArgsConstructor
public class RankingQueryController {

    private final RankingQueryService service;

    /**
     * 전체 사용자 랭킹 조회
     * @param option - commit_count, flagged_count, swear_count, sentiment_score, recent
     * @param page - 페이지 번호, 1이 default
     * @param perPage - 페이지 당 항목 수, 10이 default
     * @return 페이지네이션된 사용자 랭킹 목록
     */
    @GetMapping
    public PageResponse<RankingListResponse> getRankings(
            @RequestParam(required = false, defaultValue = "commit_count") String option,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10", name = "per_page") int perPage
    ) {
        return service.getRankings(option, page, perPage);
    }
}
