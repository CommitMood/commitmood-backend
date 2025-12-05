package com.ssafy.commitmood.domain.commit.mapper;


import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommitAnalysisMapper {

    /**
     * Commit Analysis 저장
     */
    void insert(CommitAnalysis commitAnalysis);

    /**
     * Commit Analysis 업데이트
     */
    int update(CommitAnalysis commitAnalysis);

    /**
     * ID로 Commit Analysis 조회
     */
    Optional<CommitAnalysis> findById(@Param("id") Long id);

    /**
     * Commit Log ID로 Commit Analysis 조회 (1:1 관계)
     */
    Optional<CommitAnalysis> findByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * 모든 Commit Analysis 조회
     */
    List<CommitAnalysis> findAll();

    /**
     * Commit Analysis 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * Commit Log ID로 삭제
     */
    int deleteByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * Commit Log ID로 존재 여부 확인
     */
    boolean existsByCommitLogId(@Param("commitLogId") Long commitLogId);
}