package com.ssafy.commitmood.domain.commit.mapper;


import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.commit.entity.CommitFileChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommitFileChangeMapper {

    /**
     * Commit File Change 저장
     */
    void insert(CommitFileChange commitFileChange);

    /**
     * Commit File Change 업데이트
     */
    int update(CommitFileChange commitFileChange);

    /**
     * ID로 Commit File Change 조회
     */
    Optional<CommitFileChange> findById(@Param("id") Long id);

    /**
     * Commit Log ID로 모든 File Change 조회
     */
    List<CommitFileChange> findAllByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * Commit Log ID와 Filename으로 조회
     */
    Optional<CommitFileChange> findByCommitLogIdAndFilename(
            @Param("commitLogId") Long commitLogId,
            @Param("filename") String filename
    );

    /**
     * Commit File Change 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * Commit Log ID로 모든 File Change 삭제
     */
    int deleteAllByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * Commit Log ID의 File Change 개수 조회
     */
    int countByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * 일괄 저장
     */
    void insertBatch(@Param("fileChanges") List<CommitFileChange> fileChanges);
}