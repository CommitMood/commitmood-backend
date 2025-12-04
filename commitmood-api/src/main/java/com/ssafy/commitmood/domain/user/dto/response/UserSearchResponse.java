package com.ssafy.commitmood.domain.user.dto.response;

import com.ssafy.commitmood.domain.user.entity.UserAccount;

public record UserSearchResponse(
        Long id,
        String githubLogin,
        String githubAvatarUrl
) {

    public static UserSearchResponse of(UserAccount userAccount) {
        return new UserSearchResponse(
                userAccount.getId(),
                userAccount.getGithubLogin(),
                userAccount.getGithubAvatarUrl()
        );
    }
}
