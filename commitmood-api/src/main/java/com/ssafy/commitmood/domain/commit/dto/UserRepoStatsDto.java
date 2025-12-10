package com.ssafy.commitmood.domain.commit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 저장소별 통계 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRepoStatsDto {
    private Long githubRepoId;
    private String githubRepoName;
    private String githubRepoFullName;
    private Long commitCount;
    private Long totalAdditions;
    private Long totalDeletions;
    private LocalDateTime lastCommitAt;
}