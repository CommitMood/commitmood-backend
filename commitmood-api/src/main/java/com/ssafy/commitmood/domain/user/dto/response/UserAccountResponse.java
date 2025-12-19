package com.ssafy.commitmood.domain.user.dto.response;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "사용자 계정 정보 응답 DTO")
public record UserAccountResponse(

        @Schema(description = "내부 사용자 ID", example = "12")
        Long id,

        @Schema(description = "GitHub에서 발급한 사용자 고유 ID", example = "9876543")
        Long githubUserId,

        @Schema(description = "GitHub 로그인명", example = "gildong")
        String githubLogin,

        @Schema(description = "GitHub 이메일", example = "gildong@example.com")
        String githubEmail,

        @Schema(description = "GitHub 아바타 이미지 URL",
                example = "https://avatars.githubusercontent.com/u/1234567?v=4")
        String githubAvatarUrl,

        @Schema(description = "GitHub 프로필 이름", example = "gildong Hong")
        String githubName,

        @Schema(description = "GitHub 프로필/리포지토리 마지막 동기화 시각",
                example = "2025-12-11T14:23:00")
        LocalDateTime lastSyncedAt,

        @Schema(description = "서비스 가입 시각", example = "2025-10-01T11:20:00")
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
                userAccount.getCreatedAt() // BaseTimeEntity 상속 필드
        );
    }
}