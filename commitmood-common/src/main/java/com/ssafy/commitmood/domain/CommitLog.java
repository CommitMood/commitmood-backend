package com.ssafy.commitmood.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitLog {
    private Long id;
    private Long repoId;
    private Long authorId;
    private String githubCommitSha;
    private LocalDateTime committedAt;
    private String message;
    private String htmlUrl;
    private Integer additions;
    private Integer deletions;
    private Integer totalChanges;
    private Integer filesChanged;
    private LocalDateTime createdAt;
}