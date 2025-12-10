package com.ssafy.commitmood.domain.user.dto.request;

public record UserAccountQueryCondition(
        String email,
        String name,
        String githubLogin
) {
}
