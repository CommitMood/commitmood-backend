package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "현재 인증된 사용자 정보 응답")
public record AuthMeResponse(

        @Schema(description = "사용자 ID", example = "1")
        Long id,

        @Schema(description = "GitHub 사용자 ID", example = "12345678")
        Long githubUserId,

        @Schema(description = "GitHub 로그인명", example = "octocat")
        String githubLogin,

        @Schema(description = "GitHub 이메일", example = "octocat@github.com")
        String githubEmail,

        @Schema(description = "GitHub 아바타 URL", example = "https://avatars.githubusercontent.com/u/12345678")
        String githubAvatarUrl,

        @Schema(description = "GitHub 이름", example = "The Octocat")
        String githubName,

        @Schema(description = "가입 일시", example = "2024-01-01T00:00:00")
        LocalDateTime createdAt
) {
    public static AuthMeResponse from(UserAccount userAccount) {
        return new AuthMeResponse(
                userAccount.getId(),
                userAccount.getGithubUserId(),
                userAccount.getGithubLogin(),
                userAccount.getGithubEmail(),
                userAccount.getGithubAvatarUrl(),
                userAccount.getGithubName(),
                userAccount.getCreatedAt()
        );
    }
}
