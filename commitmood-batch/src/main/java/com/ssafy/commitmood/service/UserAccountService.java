package com.ssafy.commitmood.service;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final UserAccountMapper userAccountMapper;

    public void insert(UserAccount userAccount) {
        userAccountMapper.insert(userAccount);
    }

    public Optional<UserAccount> findById(long id) {
        return userAccountMapper.findById(id);
    }

    public Optional<UserAccount> findByGithubUserId(long githubUserId) {
        return userAccountMapper.findByGithubUserId(githubUserId);
    }

    public List<UserAccount> findAll() {
        return userAccountMapper.findAll();
    }
}
