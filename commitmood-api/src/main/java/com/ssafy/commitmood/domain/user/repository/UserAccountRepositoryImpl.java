package com.ssafy.commitmood.domain.user.repository;

import com.ssafy.commitmood.domain.user.dto.request.UserAccountQueryCondition;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    public Optional<UserAccount> findByPrefixOne(UserAccountQueryCondition condition) {
        return mapper.findByPrefixOne(condition);
    }

    @Override
    public List<UserAccount> findAllByPrefix(UserAccountQueryCondition condition) {
        return mapper.findAllByPrefix(condition);
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