package com.ssafy.commitmood.domain.github.dto.response;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "GitHub 저장소 단건 응답 DTO")
public record GithubRepoResponse(

        @Schema(description = "내부 식별 ID", example = "12")
        Long id,

        @Schema(description = "해당 저장소를 소유한 사용자 ID", example = "5")
        Long userAccountId,

        @Schema(description = "GitHub에서 부여한 저장소 ID", example = "987654321")
        Long githubRepoId,

        @Schema(description = "저장소 이름", example = "commitmood-backend")
        String name,

        @Schema(description = "저장소 풀네임(org/repo)", example = "CommitMood/commitmood-backend")
        String fullName,

        @Schema(description = "기본 브랜치명", example = "main")
        String defaultBranch,

        @Schema(description = "저장소 설명", example = "CommitMood backend API service")
        String description,

        @Schema(description = "저장소 GitHub URL", example = "https://github.com/CommitMood/commitmood-backend")
        String url,

        @Schema(description = "프라이빗 여부", example = "false")
        boolean isPrivate,

        @Schema(description = "마지막 동기화 시각", example = "2025-12-10T14:20:30")
        LocalDateTime lastSyncedAt
) {

    public static GithubRepoResponse of(GithubRepo repo) {
        return new GithubRepoResponse(
                repo.getId(),
                repo.getUserAccountId(),
                repo.getGithubRepoId(),
                repo.getGithubRepoName(),
                repo.getGithubRepoFullName(),
                repo.getDefaultBranch(),
                repo.getDescription(),
                repo.getGithubRepoUrl(),
                repo.getIsPrivate(),
                repo.getLastSyncedAt()
        );
    }
}