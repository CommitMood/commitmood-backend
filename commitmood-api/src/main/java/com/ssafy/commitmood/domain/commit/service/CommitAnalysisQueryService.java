package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.repository.CommitAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommitAnalysisQueryService {
    private final CommitAnalysisRepository repository;

    public CommitAnalysisResponse getAnalysisByCommitLogId(Long id) {
        CommitAnalysis analysis = repository.findByCommitLogId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Commit analysis not found for id : " + id
                ));
        return CommitAnalysisResponse.from(analysis);
    }
}
