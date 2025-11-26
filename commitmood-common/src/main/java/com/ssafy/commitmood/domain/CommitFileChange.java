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
public class CommitFileChange {
    private Long id;
    private Long commitId;
    private String filename;
    private FileStatus status;
    private Integer additions;
    private Integer deletions;
    private Integer changes;
    private String patch;
    private String previousFilename;
    private LocalDateTime createdAt;

    public enum FileStatus {
        ADDED, MODIFIED, REMOVED, RENAMED
    }
}