package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserAccountRepository repository;
    private final UserQueryService queryService;

    public void updateUserProfileFromGithub(Long userId, GithubProfileUpdateRequest request) {
        UserAccount user = queryService.getEntityById(userId);

        user.updateProfile(
                request.githubEmail(),
                request.githubAvatarUrl(),
                request.githubName()
        );

        repository.save(user);
    }

    public void updateLastSyncedAt(Long userId, LocalDateTime now) {
        queryService.getEntityById(userId);
        repository.updateLastSyncedAt(userId, now);
    }

    public void deleteUser(Long userId) {
        queryService.getEntityById(userId);
        repository.deleteById(userId);
    }
}
