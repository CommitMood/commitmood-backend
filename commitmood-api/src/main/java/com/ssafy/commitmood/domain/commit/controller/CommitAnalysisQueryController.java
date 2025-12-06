package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.service.CommitAnalysisQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commits")
@RequiredArgsConstructor
public class CommitAnalysisQueryController {
    private final CommitAnalysisQueryService commitAnalysisQueryService;

    @GetMapping("/{commitLogId}/analysis")
    public CommitAnalysisResponse queryCommitAnalysis(@PathVariable("commitLogId") Long commitLogId) {
        return commitAnalysisQueryService.getAnalysisByCommitLogId(commitLogId);
    }
}
