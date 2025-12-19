package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import java.util.List;
import java.util.Optional;

public interface CommitAnalysisRepository {

    void save(CommitAnalysis commitAnalysis);

    Optional<CommitAnalysis> findByCommitLogId(Long commitLogId);

    List<FlaggedToken> findFlaggedTokens(Long commitLogId);
}