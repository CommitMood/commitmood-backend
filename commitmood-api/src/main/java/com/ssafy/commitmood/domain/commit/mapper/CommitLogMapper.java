package com.ssafy.commitmood.domain.commit.mapper;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommitLogMapper {

    /**
     * 커밋 로그 저장
     * @param commitLog 커밋 로그 정보
     */
    void insert(CommitLog commitLog);

    /**
     * 특정 저장소의 커밋 로그 목록 조회
     * @param repoId GitHub 저장소 ID
     * @return 커밋 로그 목록
     */
    List<CommitLog> findByRepoId(@Param("repoId") Long repoId);
}