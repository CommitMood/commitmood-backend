package com.ssafy.commitmood.domain.user.dto.response;

import com.ssafy.commitmood.domain.user.entity.UserAccount;

import java.time.LocalDateTime;

public record UserAccountResponse(
        Long id,
        Long githubUserId,
        String githubLogin,
        String githubEmail,
        String githubAvatarUrl,
        String githubName,
        LocalDateTime lastSyncedAt,
        LocalDateTime createdAt
) {

    public static UserAccountResponse of(UserAccount userAccount) {
        return new UserAccountResponse(
                userAccount.getId(),
                userAccount.getGithubUserId(),
                userAccount.getGithubLogin(),
                userAccount.getGithubEmail(),
                userAccount.getGithubAvatarUrl(),
                userAccount.getGithubName(),
                userAccount.getLastSyncedAt(),
                userAccount.getCreatedAt()   // BaseTimeEntity에서 상속
        );
    }
}