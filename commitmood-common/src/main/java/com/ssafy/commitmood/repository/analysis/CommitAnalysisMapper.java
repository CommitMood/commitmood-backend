package com.ssafy.commitmood.repository.analysis;

import com.ssafy.commitmood.domain.CommitAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommitAnalysisMapper {

    void insert(CommitAnalysis commitAnalysis);

    Optional<CommitAnalysis> findById(@Param("id") Long id);

    Optional<CommitAnalysis> findByCommitId(@Param("commitId") Long commitId);

    List<CommitAnalysis> findByCommitIds(@Param("commitIds") List<Long> commitIds);

    void update(CommitAnalysis commitAnalysis);

    void deleteById(@Param("id") Long id);

    void deleteByCommitId(@Param("commitId") Long commitId);
}