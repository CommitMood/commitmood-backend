package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenResponse;
import com.ssafy.commitmood.domain.commit.service.CommitAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Commit Analysis", description = "커밋 분석 API")
@RestController
@RequestMapping("commits")
@RequiredArgsConstructor
public class CommitAnalysisController {

    private final CommitAnalysisService commitAnalysisService;

    @Operation(summary = "커밋 분석 정보 조회", description = "특정 커밋의 감정 분석 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = CommitAnalysisResponse.class))),
            @ApiResponse(responseCode = "404", description = "커밋 분석 정보를 찾을 수 없습니다.", content = @Content)
    })
    @GetMapping("/{commitLogId}/analysis")
    public ResponseEntity<CommitAnalysisResponse> getCommitAnalysis(
            @PathVariable Long commitLogId
    ) {
        CommitAnalysisResponse response = commitAnalysisService.getCommitAnalysis(commitLogId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "플래그 토큰 목록 조회", description = "특정 커밋에서 감지된 플래그 토큰 목록을 조회합니다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = FlaggedTokenResponse.class))),
            @ApiResponse(responseCode = "404", description = "커밋을 찾을 수 없습니다.", content = @Content)
    })
    @GetMapping("/{commitLogId}/tokens")
    public ResponseEntity<List<FlaggedTokenResponse>> getFlaggedTokens(
            @PathVariable Long commitLogId
    ) {
        List<FlaggedTokenResponse> response = commitAnalysisService.getFlaggedTokens(commitLogId);
        return ResponseEntity.ok(response);
    }
}