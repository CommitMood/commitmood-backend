package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.common.exception.NotFoundException;
import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserAccountRepository repository;

    public void updateUserProfileFromGithub(Long userId, GithubProfileUpdateRequest request) {
        UserAccount user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found. id=" + userId));

        user.updateProfile(
                request.githubEmail(),
                request.githubAvatarUrl(),
                request.githubName()
        );

        repository.update(user);
    }

    public void updateLastSyncedAt(Long userId, LocalDateTime now) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found. id=" + userId));

        repository.updateLastSyncedAt(userId, now);
    }

    public void deleteUser(Long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found. id=" + userId));

        repository.deleteById(userId);
    }
}
