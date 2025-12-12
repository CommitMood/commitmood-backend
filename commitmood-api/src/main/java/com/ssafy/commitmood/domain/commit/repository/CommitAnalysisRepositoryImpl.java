package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.mapper.CommitAnalysisMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommitAnalysisRepositoryImpl implements CommitAnalysisRepository {

    private final CommitAnalysisMapper mapper;

    @Override
    public void save(CommitAnalysis commitAnalysis) {
        mapper.insert(commitAnalysis);
    }

    @Override
    public Optional<CommitAnalysis> findByCommitLogId(Long commitLogId) {
        return mapper.findByCommitLogId(commitLogId);
    }

    @Override
    public List<FlaggedToken> findFlaggedTokens(Long commitLogId) {
        return mapper.findFlaggedTokensByCommitLogId(commitLogId);
    }
}