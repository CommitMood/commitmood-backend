package com.ssafy.commitmood.domain.commit.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;

@Mapper
public interface GithubRepoInsertMapper {

    /**
     * GitHub 저장소 정보 저장
     * @param githubRepo GitHub 저장소
     */
    void insert(GithubRepo githubRepo);
}