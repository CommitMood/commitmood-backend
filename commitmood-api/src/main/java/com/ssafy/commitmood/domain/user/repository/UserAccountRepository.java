package com.ssafy.commitmood.domain.user.repository;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserAccountRepository {

    private final UserAccountMapper mapper;

    public void save(UserAccount userAccount) {
        mapper.insert(userAccount);
    }

    public Optional<UserAccount> findById(Long id) {
        return mapper.findById(id);
    }

    public Optional<UserAccount> findByGithubUserId(Long githubUserId) {
        return mapper.findByGithubUserId(githubUserId);
    }

    public Optional<UserAccount> findByGithubLogin(String githubLogin) {
        return mapper.findByGithubLogin(githubLogin);
    }

    public List<UserAccount> searchByLoginLike(String query) {
        return mapper.searchByLoginLike(query);
    }

    public List<UserAccount> searchByLoginPaged(
            @Param("keyword") String keyword,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset
    ) {
        return mapper.searchByLoginPaged(keyword, pageSize, offset);
    }

    public void update(UserAccount userAccount) {
        mapper.update(userAccount);
    }

    public void updateLastSyncedAt(Long id, LocalDateTime lastSyncedAt) {
        mapper.updateLastSyncedAt(id, lastSyncedAt);
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    public long countByLogin(String keyword) {
        return mapper.countByLogin(keyword);
    }
}
