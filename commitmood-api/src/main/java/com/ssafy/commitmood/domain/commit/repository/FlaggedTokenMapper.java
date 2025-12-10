package com.ssafy.commitmood.domain.commit.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;

@Mapper
public interface FlaggedTokenMapper {

    /**
     * 플래그 토큰 저장
     * @param flaggedToken 플래그 토큰
     */
    void insert(FlaggedToken flaggedToken);
}