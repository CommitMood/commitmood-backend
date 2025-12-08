package com.ssafy.commitmood.domain.user.repository;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserAccountRepositoryImpl implements UserAccountRepository {

    private final UserAccountMapper mapper;

    @Override
    public void save(UserAccount userAccount) {
        mapper.insert(userAccount);
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        return mapper.findById(id);
    }

    @Override
    public Optional<UserAccount> findByGithubUserId(Long githubUserId) {
        return mapper.findByGithubUserId(githubUserId);
    }

    @Override
    public Optional<UserAccount> findByGithubLogin(String githubLogin) {
        return mapper.findByGithubLogin(githubLogin);
    }

    @Override
    public List<UserAccount> searchByLoginLike(String query) {
        return mapper.searchByLoginLike(query);
    }

    @Override
    public List<UserAccount> searchByLoginPaged(String keyword, int pageSize, int offset) {
        return mapper.searchByLoginPaged(keyword, pageSize, offset);
    }

    @Override
    public void update(UserAccount userAccount) {
        mapper.update(userAccount);
    }

    @Override
    public void updateLastSyncedAt(Long id, LocalDateTime lastSyncedAt) {
        mapper.updateLastSyncedAt(id, lastSyncedAt);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public long countByLogin(String keyword) {
        return mapper.countByLogin(keyword);
    }
}