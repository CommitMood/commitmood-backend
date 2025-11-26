package com.ssafy.commitmood.repository.commit;

import com.ssafy.commitmood.domain.CommitLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface CommitLogMapper {

    void insert(CommitLog commitLog);

    Optional<CommitLog> findById(@Param("id") Long id);

    Optional<CommitLog> findByRepoIdAndSha(
            @Param("repoId") Long repoId,
            @Param("githubCommitSha") String githubCommitSha
    );

    List<CommitLog> findByAuthorId(@Param("authorId") Long authorId);

    List<CommitLog> findByAuthorIdAndDateRange(
            @Param("authorId") Long authorId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<CommitLog> findByRepoId(@Param("repoId") Long repoId);

    void update(CommitLog commitLog);

    void deleteById(@Param("id") Long id);

    void deleteByRepoId(@Param("repoId") Long repoId);
}