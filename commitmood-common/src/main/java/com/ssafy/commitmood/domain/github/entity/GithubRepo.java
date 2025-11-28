package com.ssafy.commitmood.domain.github.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "githubRepoId")
public class GithubRepo extends BaseTimeEntity {

    private Long id;
    private Long userAccountId;
    private Long githubRepoId;
    private String githubRepoName;
    private String githubRepoFullName;
    private String defaultBranch;
    private String description;
    private String githubRepoUrl;
    private Boolean isPrivate;
    private LocalDateTime lastSyncedAt;

    private GithubRepo(
            Long userAccountId,
            Long githubRepoId,
            String githubRepoName,
            String githubRepoFullName,
            String defaultBranch,
            String description,
            String githubRepoUrl,
            Boolean isPrivate
    ) {
        this.userAccountId = userAccountId;
        this.githubRepoId = githubRepoId;
        this.githubRepoName = githubRepoName;
        this.githubRepoFullName = githubRepoFullName;
        this.defaultBranch = defaultBranch;
        this.description = description;
        this.githubRepoUrl = githubRepoUrl;
        this.isPrivate = isPrivate != null ? isPrivate : false;
    }

    public static GithubRepo create(
            Long userAccountId,
            Long githubRepoId,
            String githubRepoName,
            String githubRepoFullName,
            String defaultBranch,
            String description,
            String githubRepoUrl,
            Boolean isPrivate
    ) {
        validate(userAccountId, githubRepoId, githubRepoName, githubRepoFullName);
        return new GithubRepo(
                userAccountId,
                githubRepoId,
                githubRepoName,
                githubRepoFullName,
                defaultBranch,
                description,
                githubRepoUrl,
                isPrivate
        );
    }

    public void updateInfo(
            String githubRepoName,
            String defaultBranch,
            String description,
            String githubRepoUrl,
            Boolean isPrivate
    ) {
        this.githubRepoName = githubRepoName;
        this.defaultBranch = defaultBranch;
        this.description = description;
        this.githubRepoUrl = githubRepoUrl;
        if (isPrivate != null) {
            this.isPrivate = isPrivate;
        }
    }

    public void updateLastSyncedAt(LocalDateTime lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }

    private static void validate(
            Long userAccountId,
            Long githubRepoId,
            String githubRepoName,
            String githubRepoFullName
    ) {
        if (userAccountId == null) {
            throw new IllegalArgumentException("User Account ID는 필수입니다.");
        }
        if (githubRepoId == null) {
            throw new IllegalArgumentException("GitHub Repository ID는 필수입니다.");
        }
        if (githubRepoName == null || githubRepoName.isBlank()) {
            throw new IllegalArgumentException("Repository Name은 비어 있을 수 없습니다.");
        }
        if (githubRepoFullName == null || githubRepoFullName.isBlank()) {
            throw new IllegalArgumentException("Repository Full Name은 비어 있을 수 없습니다.");
        }
    }
}