package com.ssafy.commitmood.domain.commit.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = {"githubRepoId", "githubCommitSha"}, callSuper = false)
public class CommitLog extends BaseTimeEntity {

    private Long id;
    private Long githubRepoId;
    private Long userAccountId;
    private String githubCommitSha;
    private LocalDateTime committedAt;
    private String message;
    private String htmlUrl;
    private Long additions;
    private Long deletions;
    private Long totalChanges;
    private Long filesChanged;

    private CommitLog(
            Long githubRepoId,
            Long userAccountId,
            String githubCommitSha,
            LocalDateTime committedAt,
            String message,
            String htmlUrl,
            Long additions,
            Long deletions,
            Long totalChanges,
            Long filesChanged
    ) {
        this.githubRepoId = githubRepoId;
        this.userAccountId = userAccountId;
        this.githubCommitSha = githubCommitSha;
        this.committedAt = committedAt;
        this.message = message;
        this.htmlUrl = htmlUrl;
        this.additions = additions != null ? additions : 0L;
        this.deletions = deletions != null ? deletions : 0L;
        this.totalChanges = totalChanges != null ? totalChanges : 0L;
        this.filesChanged = filesChanged;
    }

    public static CommitLog create(
            Long githubRepoId,
            Long userAccountId,
            String githubCommitSha,
            LocalDateTime committedAt,
            String message,
            String htmlUrl,
            Long additions,
            Long deletions,
            Long totalChanges,
            Long filesChanged
    ) {
        validate(githubRepoId, userAccountId, githubCommitSha, committedAt, message);
        return new CommitLog(
                githubRepoId,
                userAccountId,
                githubCommitSha,
                committedAt,
                message,
                htmlUrl,
                additions,
                deletions,
                totalChanges,
                filesChanged
        );
    }

    public void updateStats(
            Long additions,
            Long deletions,
            Long totalChanges,
            Long filesChanged
    ) {
        this.additions = additions != null ? additions : this.additions;
        this.deletions = deletions != null ? deletions : this.deletions;
        this.totalChanges = totalChanges != null ? totalChanges : this.totalChanges;
        this.filesChanged = filesChanged;
    }

    public void updateMessage(String message) {
        if (message != null && !message.isBlank()) {
            this.message = message;
        }
    }

    private static void validate(
            Long githubRepoId,
            Long userAccountId,
            String githubCommitSha,
            LocalDateTime committedAt,
            String message
    ) {
        if (githubRepoId == null) {
            throw new IllegalArgumentException("GitHub Repository ID는 필수입니다.");
        }
        if (userAccountId == null) {
            throw new IllegalArgumentException("User Account ID는 필수입니다.");
        }
        if (githubCommitSha == null || githubCommitSha.isBlank()) {
            throw new IllegalArgumentException("Commit SHA는 비어 있을 수 없습니다.");
        }
        if (githubCommitSha.length() != 40) {
            throw new IllegalArgumentException("Commit SHA는 40자여야 합니다.");
        }
        if (committedAt == null) {
            throw new IllegalArgumentException("Committed At은 필수입니다.");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Commit Message는 비어 있을 수 없습니다.");
        }
    }
}