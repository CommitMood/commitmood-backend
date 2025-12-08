package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.common.dto.response.PageResponse;
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

    @GetMapping
    public PageResponse<?> getRankings(
            @RequestParam(required = false, defaultValue = "commit_count") String option,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int perPage
    ) {
        return service.getRankings(option, page, perPage);
    }
}
