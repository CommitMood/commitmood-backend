package com.ssafy.commitmood.domain.github.dto.response;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import java.time.LocalDateTime;

public record GithubRepoResponse(
        Long id,
        Long userAccountId,
        Long githubRepoId,
        String name,
        String fullName,
        String defaultBranch,
        String description,
        String url,
        boolean isPrivate,
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
