package com.ssafy.commitmood.repository.user;

import com.ssafy.commitmood.domain.AccessToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AccessTokenMapper {

    void insert(AccessToken accessToken);

    Optional<AccessToken> findById(@Param("id") Long id);

    Optional<AccessToken> findByAccessToken(@Param("accessToken") String accessToken);

    List<AccessToken> findByUserId(@Param("userId") Long userId);

    void update(AccessToken accessToken);

    void deleteById(@Param("id") Long id);

    void deleteByUserId(@Param("userId") Long userId);
}