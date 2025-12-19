package com.ssafy.commitmood.domain.github.mapper;


import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GithubRepoMapper {

    /**
     * GitHub Repository 저장
     */
    void insert(GithubRepo githubRepo);

    /**
     * GitHub Repository 업데이트
     */
    int update(GithubRepo githubRepo);

    /**
     * ID로 GitHub Repository 조회
     */
    Optional<GithubRepo> findById(@Param("id") Long id);

    /**
     * GitHub Repository ID로 조회
     */
    Optional<GithubRepo> findByGithubRepoId(@Param("githubRepoId") Long githubRepoId);

    /**
     * GitHub Repository Full Name으로 조회
     */
    Optional<GithubRepo> findByGithubRepoFullName(@Param("githubRepoFullName") String githubRepoFullName);

    /**
     * User Account ID로 모든 Repository 조회
     */
    List<GithubRepo> findAllByUserAccountId(@Param("userAccountId") Long userAccountId);

    /**
     * User Account ID와 Private 여부로 Repository 조회
     */
    List<GithubRepo> findAllByUserAccountIdAndPrivacy(
            @Param("userAccountId") Long userAccountId,
            @Param("isPrivate") Boolean isPrivate
    );

    /**
     * 모든 Repository 조회
     */
    List<GithubRepo> findAll();

    /**
     * Repository 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * GitHub Repository ID로 존재 여부 확인
     */
    boolean existsByGithubRepoId(@Param("githubRepoId") Long githubRepoId);

    /**
     * User Account ID의 Repository 개수 조회
     */
    int countByUserAccountId(@Param("userAccountId") Long userAccountId);
}