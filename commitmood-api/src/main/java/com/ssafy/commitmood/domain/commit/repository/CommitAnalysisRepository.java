package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.repository.mapper.CommitAnalysisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommitAnalysisRepository {

    private final CommitAnalysisMapper mapper;

    public Optional<CommitAnalysis> findByCommitLogId(Long commitLogId) {
        return mapper.findByCommitLogId(commitLogId);
    }
}
