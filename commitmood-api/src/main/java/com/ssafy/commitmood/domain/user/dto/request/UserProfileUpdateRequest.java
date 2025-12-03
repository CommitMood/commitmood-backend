package com.ssafy.commitmood.domain.user.dto.request;

public record UserProfileUpdateRequest(
        String githubEmail,
        String githubAvatarUrl,
        String githubName
) {
}
