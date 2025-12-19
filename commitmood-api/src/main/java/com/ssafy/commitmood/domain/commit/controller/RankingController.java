package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.common.RankingOptionsEnum;
import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.UserRankingDetailResponse;
import com.ssafy.commitmood.domain.commit.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ranking", description = "사용자 대상, 전체 랭킹 API")
@RestController
@RequestMapping("ranking")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "전체 사용자 랭킹 조회", description = "다양한 기준으로 사용자 랭킹 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = RankingListResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터", content = @Content)
    })
    @GetMapping
    public ResponseEntity<RankingListResponse> getRankingList(
            @Parameter(description = "랭킹 기준")
            @RequestParam(defaultValue = "commit_count") RankingOptionsEnum option,

            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "per_page", defaultValue = "30") int perPage
    ) {
        RankingListResponse response = rankingService.getRankingList(option, page, perPage);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "특정 사용자 랭킹 상세 조회", description = "특정 사용자의 상세 랭킹 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserRankingDetailResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터", content = @Content)
    })
    @GetMapping("/{userAccountId}")
    public ResponseEntity<UserRankingDetailResponse> getUserRankingDetail(
            @Parameter(description = "사용자 계정 ID", required = true)
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