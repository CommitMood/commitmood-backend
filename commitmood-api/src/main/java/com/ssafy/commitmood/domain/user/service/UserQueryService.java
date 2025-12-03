package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.common.exception.NotFoundException;
import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserAccountRepository repository;

    public UserAccountResponse getUserById(Long id) {
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("UserAccount not found. id=" + id));
        return UserAccountResponse.of(user);
    }

    public UserAccountResponse getUserByGithubLogin(String githubLogin) {
        UserAccount user = repository.findByGithubLogin(githubLogin)
                .orElseThrow(() -> new NotFoundException("UserAccount not found. githubLogin=" + githubLogin));
        return UserAccountResponse.of(user);
    }

    public UserAccount getEntityById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("UserAccount not found. id=" + id));
    }

    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }
}