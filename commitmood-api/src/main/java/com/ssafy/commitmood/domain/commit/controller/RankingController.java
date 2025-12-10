package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.common.RankingOptionsEnum;
import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.UserRankingDetailResponse;
import com.ssafy.commitmood.domain.commit.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<RankingListResponse> getRankingList(
            @RequestParam(defaultValue = "commit_count") RankingOptionsEnum option,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "30") int perPage
    ) {
        RankingListResponse response = rankingService.getRankingList(option, page, perPage);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userAccountId}")
    public ResponseEntity<UserRankingDetailResponse> getUserRankingDetail(
            @PathVariable Long userAccountId,
            @RequestParam UserRankingDetailOptions detail,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "30") int perPage
    ) {
        UserRankingDetailResponse response = rankingService.getUserRankingDetail(
                userAccountId, detail, page, perPage
        );
        return ResponseEntity.ok(response);
    }
}