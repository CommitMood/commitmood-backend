package com.ssafy.commitmood.domain.commit.repository.mapper;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface CommitAnalysisMapper {
    Optional<CommitAnalysis> findByCommitLogId(Long commitLogId);
}
