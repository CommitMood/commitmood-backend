package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenResponse;
import com.ssafy.commitmood.domain.commit.service.CommitAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("commits")
@RequiredArgsConstructor
public class CommitAnalysisController {

    private final CommitAnalysisService commitAnalysisService;

    @GetMapping("/{commitLogId}/analysis")
    public ResponseEntity<CommitAnalysisResponse> getCommitAnalysis(
            @PathVariable Long commitLogId
    ) {
        CommitAnalysisResponse response = commitAnalysisService.getCommitAnalysis(commitLogId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{commitLogId}/tokens")
    public ResponseEntity<List<FlaggedTokenResponse>> getFlaggedTokens(
            @PathVariable Long commitLogId
    ) {
        List<FlaggedTokenResponse> response = commitAnalysisService.getFlaggedTokens(commitLogId);
        return ResponseEntity.ok(response);
    }
}