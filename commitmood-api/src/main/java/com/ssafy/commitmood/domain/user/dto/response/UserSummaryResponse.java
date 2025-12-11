package com.ssafy.commitmood.domain.user.dto.response;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 요약 정보 응답 DTO")
public record UserSummaryResponse(

        @Schema(description = "사용자 ID", example = "1")
        Long id,

        @Schema(description = "GitHub 로그인명", example = "honggildong")
        String githubLogin,

        @Schema(
                description = "GitHub 아바타 이미지 URL",
                example = "https://avatars.githubusercontent.com/u/1234567?v=4"
        )
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
