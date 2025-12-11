package com.ssafy.commitmood.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "GitHub 프로필 업데이트 요청 DTO")
public record GithubProfileUpdateRequest(

        @Schema(description = "GitHub 계정 이메일", example = "shkim@example.com")
        String githubEmail,

        @Schema(description = "GitHub 프로필 아바타 URL", example = "https://avatars.githubusercontent.com/u/1234567?v=4")
        String githubAvatarUrl,

        @Schema(description = "GitHub 프로필 이름", example = "devys")
        String githubName
) {
}
