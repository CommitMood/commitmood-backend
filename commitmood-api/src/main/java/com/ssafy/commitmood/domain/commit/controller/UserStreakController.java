package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.UserStreakResponse;
import com.ssafy.commitmood.domain.commit.service.UserStreakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("streak")
@RequiredArgsConstructor
public class UserStreakController {

    private final UserStreakService userStreakService;

    @Operation(summary = "사용자 커밋 스트릭 조회", description = "특정 사용자의 커밋 스트릭 정보를 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = UserStreakResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content),
            @ApiResponse(responseCode = "400", description = "잘못된 옵션 값", content = @Content)
    })
    @GetMapping("/{userAccountId}")
    public ResponseEntity<UserStreakResponse> getUserStreak(
            @PathVariable Long userAccountId,
            @RequestParam(defaultValue = "month") String option
    ) {
        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, option);
        return ResponseEntity.ok(response);
    }
}