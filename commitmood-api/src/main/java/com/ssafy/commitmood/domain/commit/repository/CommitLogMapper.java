package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommitLogMapper {

    void insert(CommitLog commitLog);

    List<CommitLog> findByRepoId(@Param("repoId") Long repoId);
}