package com.ssafy.commitmood.domain.commit.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;

@Mapper
public interface CommitLogMapper {

    /**
     * 커밋 로그 저장
     * @param commitLog 커밋 로그
     */
    void insert(CommitLog commitLog);
}