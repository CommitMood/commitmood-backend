package com.ssafy.commitmood.domain.user.repository;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserAccountRepository {

    void save(UserAccount userAccount);

    Optional<UserAccount> findById(Long id);

    Optional<UserAccount> findByGithubUserId(Long githubUserId);

    Optional<UserAccount> findByGithubLogin(String githubLogin);

    List<UserAccount> searchByLoginLike(String query);

    List<UserAccount> searchByLoginPaged(String keyword, int pageSize, int offset);

    void update(UserAccount userAccount);

    void updateLastSyncedAt(Long id, LocalDateTime lastSyncedAt);

    void deleteById(Long id);

    long countByLogin(String keyword);
}