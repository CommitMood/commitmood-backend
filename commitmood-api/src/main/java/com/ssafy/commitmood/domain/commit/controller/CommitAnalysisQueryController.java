package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.service.CommitAnalysisQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commits")
@RequiredArgsConstructor
public class CommitAnalysisQueryController {
    private final CommitAnalysisQueryService commitAnalysisQueryService;

    /**
     * 특정 커밋의 분석 결과 조회
     * @param commitLogId 커밋 로그 ID
     * @return 커밋의 분석 결과, 만약 구체적인 토큰이 필요하다면 tokens로 조회
     */
    @GetMapping("/{commitLogId}/analysis")
    public CommitAnalysisResponse queryCommitAnalysis(@PathVariable("commitLogId") Long commitLogId) {
        return commitAnalysisQueryService.getAnalysisByCommitLogId(commitLogId);
    }
}
