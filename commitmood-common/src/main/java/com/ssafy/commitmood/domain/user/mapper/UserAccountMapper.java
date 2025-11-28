package com.ssafy.commitmood.domain.user.mapper;

import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {

    /**
     * 사용자 계정 저장
     */
    void insert(UserAccount userAccount);

    /**
     * 사용자 계정 업데이트
     */
    int update(UserAccount userAccount);

    /**
     * ID로 사용자 계정 조회
     */
    Optional<UserAccount> findById(@Param("id") Long id);

    /**
     * GitHub User ID로 사용자 계정 조회
     */
    Optional<UserAccount> findByGithubUserId(@Param("githubUserId") Long githubUserId);

    /**
     * GitHub Login으로 사용자 계정 조회
     */
    Optional<UserAccount> findByGithubLogin(@Param("githubLogin") String githubLogin);

    /**
     * 모든 사용자 계정 조회
     */
    List<UserAccount> findAll();

    /**
     * 사용자 계정 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * GitHub User ID로 사용자 계정 존재 여부 확인
     */
    boolean existsByGithubUserId(@Param("githubUserId") Long githubUserId);

    /**
     * GitHub Login으로 사용자 계정 존재 여부 확인
     */
    boolean existsByGithubLogin(@Param("githubLogin") String githubLogin);
}