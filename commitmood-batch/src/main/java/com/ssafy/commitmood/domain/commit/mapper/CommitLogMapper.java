package com.ssafy.commitmood.domain.commit.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommitLogMapper {

    /**
     * Commit Log 저장
     */
    void insert(CommitLog commitLog);

    /**
     * Commit Log 업데이트
     */
    int update(CommitLog commitLog);

    /**
     * ID로 Commit Log 조회
     */
    Optional<CommitLog> findById(@Param("id") Long id);

    /**
     * Repository ID와 Commit SHA로 조회
     */
    Optional<CommitLog> findByRepoIdAndSha(
            @Param("githubRepoId") Long githubRepoId,
            @Param("githubCommitSha") String githubCommitSha
    );

    /**
     * Repository ID로 모든 Commit Log 조회
     */
    List<CommitLog> findAllByGithubRepoId(@Param("githubRepoId") Long githubRepoId);

    /**
     * User Account ID로 모든 Commit Log 조회
     */
    List<CommitLog> findAllByUserAccountId(@Param("userAccountId") Long userAccountId);

    /**
     * User Account ID와 기간으로 Commit Log 조회
     */
    List<CommitLog> findAllByUserAccountIdAndDateRange(
            @Param("userAccountId") Long userAccountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Repository ID와 기간으로 Commit Log 조회
     */
    List<CommitLog> findAllByGithubRepoIdAndDateRange(
            @Param("githubRepoId") Long githubRepoId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Commit Log 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * Repository ID와 Commit SHA로 존재 여부 확인
     */
    boolean existsByRepoIdAndSha(
            @Param("githubRepoId") Long githubRepoId,
            @Param("githubCommitSha") String githubCommitSha
    );

    /**
     * User Account ID의 Commit 개수 조회
     */
    int countByUserAccountId(@Param("userAccountId") Long userAccountId);

    /**
     * Repository ID의 Commit 개수 조회
     */
    int countByGithubRepoId(@Param("githubRepoId") Long githubRepoId);
}