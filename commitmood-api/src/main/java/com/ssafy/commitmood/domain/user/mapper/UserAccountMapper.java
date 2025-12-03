package com.ssafy.commitmood.domain.user.mapper;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAccountMapper {

    void insert(UserAccount userAccount);

    Optional<UserAccount> findById(@Param("id") Long id);

    Optional<UserAccount> findByGithubUserId(@Param("githubUserId") Long githubUserId);

    Optional<UserAccount> findByGithubLogin(@Param("githubLogin") String githubLogin);

    List<UserAccount> searchByLogin(@Param("query") String query);

    List<UserAccount> searchByLoginPaged(
            @Param("keyword") String keyword,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset
    );

    void update(UserAccount userAccount);

    void updateLastSyncedAt(
            @Param("id") Long id,
            @Param("lastSyncedAt") LocalDateTime lastSyncedAt
    );

    void deleteById(@Param("id") Long id);
}