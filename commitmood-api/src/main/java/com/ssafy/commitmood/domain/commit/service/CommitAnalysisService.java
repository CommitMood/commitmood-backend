package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.repository.CommitAnalysisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommitAnalysisService {

    private final CommitAnalysisMapper commitAnalysisMapper;

    public CommitAnalysisResponse getCommitAnalysis(Long commitLogId) {
        CommitAnalysis analysis = commitAnalysisMapper.findByCommitLogId(commitLogId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "커밋 분석 정보를 찾을 수 없습니다. commitLogId: " + commitLogId));

        return CommitAnalysisResponse.from(analysis);
    }

    public List<FlaggedTokenResponse> getFlaggedTokens(Long commitLogId) {
        return commitAnalysisMapper.findFlaggedTokensByCommitLogId(commitLogId)
                .stream()
                .map(FlaggedTokenResponse::from)
                .collect(Collectors.toList());
    }
}
