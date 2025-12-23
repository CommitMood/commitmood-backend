package com.ssafy.commitmood.security.mapper;

import com.ssafy.commitmood.domain.auth.entity.AccessToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface AccessTokenMapper {

    void insert(AccessToken accessToken);

    Optional<AccessToken> findByUserAccountId(@Param("userAccountId") Long userAccountId);

    int deleteByUserAccountId(@Param("userAccountId") Long userAccountId);
}
