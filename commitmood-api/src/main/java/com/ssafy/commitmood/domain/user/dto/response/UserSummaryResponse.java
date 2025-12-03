package com.ssafy.commitmood.domain.user.dto.response;

import com.ssafy.commitmood.domain.user.entity.UserAccount;

public record UserSummaryResponse(
        Long id,
        String githubLogin,
        String githubAvatarUrl
) {

    public static UserSummaryResponse of(UserAccount userAccount) {
        return new UserSummaryResponse(
                userAccount.getId(),
                userAccount.getGithubLogin(),
                userAccount.getGithubAvatarUrl()
        );
    }
}
