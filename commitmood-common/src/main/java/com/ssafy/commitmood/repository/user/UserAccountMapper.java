package com.ssafy.commitmood.repository.user;


import com.ssafy.commitmood.domain.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserAccountMapper {

    void insert(UserAccount userAccount);

    Optional<UserAccount> findById(@Param("userId") Long userId);

    Optional<UserAccount> findByGithubId(@Param("githubId") String githubId);

    Optional<UserAccount> findByGithubLogin(@Param("githubLogin") String githubLogin);

    void update(UserAccount userAccount);

    void deleteById(@Param("userId") Long userId);
}