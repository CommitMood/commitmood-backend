package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.common.exception.NotFoundException;
import com.ssafy.commitmood.domain.user.dto.request.UserAccountQueryCondition;
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

    public UserAccountResponse getUserByEmail(String email) {
        UserAccount user = repository.findByPrefixOne(
                new UserAccountQueryCondition(email, null, null)
        ).orElseThrow(() -> new NotFoundException("UserAccount not found. email=" + email));

        return UserAccountResponse.of(user);
    }

    public UserAccountResponse getUserByName(String name) {
        UserAccount user = repository.findByPrefixOne(
                new UserAccountQueryCondition(null, name, null)
        ).orElseThrow(() -> new NotFoundException("UserAccount not found. name=" + name));

        return UserAccountResponse.of(user);
    }

    public UserAccountResponse getUserByGithubLogin(String githubLogin) {
        UserAccount user = repository.findByPrefixOne(
                new UserAccountQueryCondition(null, null, githubLogin)
        ).orElseThrow(() -> new NotFoundException("UserAccount not found. githubLogin=" + githubLogin));

        return UserAccountResponse.of(user);
    }

    public UserAccountResponse getUserById(Long id) {
        UserAccount user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("UserAccount not found. id=" + id));

        return UserAccountResponse.of(user);
    }

    public boolean existsById(Long id) {
        return repository.findById(id).isPresent();
    }
}