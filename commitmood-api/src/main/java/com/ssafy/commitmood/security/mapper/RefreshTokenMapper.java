package com.ssafy.commitmood.security.mapper;

import com.ssafy.commitmood.domain.auth.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface RefreshTokenMapper {

    void insert(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(@Param("token") String token);

    Optional<RefreshToken> findByUserAccountId(@Param("userAccountId") Long userAccountId);

    int deleteByUserAccountId(@Param("userAccountId") Long userAccountId);

    int deleteByToken(@Param("token") String token);

    int deleteExpiredTokens();
}
