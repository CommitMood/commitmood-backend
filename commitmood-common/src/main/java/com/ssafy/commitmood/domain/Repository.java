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
public class Repository {
    private Long id;
    private Long ownerId;
    private Long githubRepoId;
    private String name;
    private String fullName;
    private String defaultBranch;
    private String description;
    private String htmlUrl;
    private Boolean isPrivate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastSyncedAt;
}

