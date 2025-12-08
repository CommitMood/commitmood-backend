package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenListResponse;
import com.ssafy.commitmood.domain.commit.service.CommitTokenQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commits")
@RequiredArgsConstructor
public class CommitTokenQueryController {

    private final CommitTokenQueryService service;

    /**
     * 특정 커밋의 의미가 있는 토큰(flagged token)들을 조회
     * @param commitLogId 커밋 로그 ID
     * @return 의미 있는 토큰 리스트
     */
    @GetMapping("/{commitLogId}/tokens")
    public FlaggedTokenListResponse getCommitTokens(@PathVariable Long commitLogId) {
        return service.getTokensByCommitLogId(commitLogId);
    }
}
