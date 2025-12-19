package com.ssafy.commitmood.domain.auth.mapper;

import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.auth.entity.AccessToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AccessTokenMapper {

    /**
     * Access Token 저장
     */
    void insert(AccessToken accessToken);

    /**
     * Access Token 업데이트
     */
    int update(AccessToken accessToken);

    /**
     * ID로 Access Token 조회
     */
    Optional<AccessToken> findById(@Param("id") Long id);

    /**
     * User Account ID로 Access Token 조회
     */
    Optional<AccessToken> findByUserAccountId(@Param("userAccountId") Long userAccountId);

    /**
     * Access Token 문자열로 조회
     */
    Optional<AccessToken> findByAccessToken(@Param("accessToken") String accessToken);

    /**
     * User Account ID로 모든 Access Token 조회
     */
    List<AccessToken> findAllByUserAccountId(@Param("userAccountId") Long userAccountId);

    /**
     * Access Token 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * User Account ID로 Access Token 삭제
     */
    int deleteByUserAccountId(@Param("userAccountId") Long userAccountId);

    /**
     * Access Token 존재 여부 확인
     */
    boolean existsByAccessToken(@Param("accessToken") String accessToken);
}