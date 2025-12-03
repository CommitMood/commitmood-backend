package com.ssafy.commitmood.domain.user.dto.request;

public record GithubProfileUpdateRequest(
        String githubEmail,
        String githubAvatarUrl,
        String githubName
) {
}
