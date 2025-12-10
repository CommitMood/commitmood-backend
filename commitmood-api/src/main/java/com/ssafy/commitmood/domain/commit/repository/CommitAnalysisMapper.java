package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CommitAnalysisMapper {

    /**
     * 커밋 분석 정보 저장
     * @param commitAnalysis 커밋 분석 정보
     */
    void insert(CommitAnalysis commitAnalysis);

    /**
     * 특정 커밋의 분석 정보 조회
     * @param commitLogId 커밋 로그 ID
     * @return 커밋 분석 정보
     */
    Optional<CommitAnalysis> findByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * 특정 커밋의 플래그된 토큰 목록 조회
     * @param commitLogId 커밋 로그 ID
     * @return 플래그된 토큰 목록
     */
    List<FlaggedToken> findFlaggedTokensByCommitLogId(@Param("commitLogId") Long commitLogId);
}