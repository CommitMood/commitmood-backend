package com.ssafy.commitmood.domain.commit.entity;

import static lombok.AccessLevel.PROTECTED;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class CommitFileChange extends BaseTimeEntity {

    private Long id;
    private Long commitLogId;
    private String filename;
    private String prevFilename;
    private FileChangeStatus status;
    private Long additions;
    private Long deletions;
    private Long changes;
    private String patch;

    private CommitFileChange(
            Long commitLogId,
            String filename,
            String prevFilename,
            FileChangeStatus status,
            Long additions,
            Long deletions,
            Long changes,
            String patch
    ) {
        this.commitLogId = commitLogId;
        this.filename = filename;
        this.prevFilename = prevFilename;
        this.status = status;
        this.additions = additions != null ? additions : 0L;
        this.deletions = deletions != null ? deletions : 0L;
        this.changes = changes != null ? changes : 0L;
        this.patch = patch;
    }

    public static CommitFileChange create(
            Long commitLogId,
            String filename,
            String prevFilename,
            FileChangeStatus status,
            Long additions,
            Long deletions,
            Long changes,
            String patch
    ) {
        validate(commitLogId, filename, status);
        return new CommitFileChange(
                commitLogId,
                filename,
                prevFilename,
                status,
                additions,
                deletions,
                changes,
                patch
        );
    }

    public void updateStats(Long additions, Long deletions, Long changes) {
        this.additions = additions != null ? additions : this.additions;
        this.deletions = deletions != null ? deletions : this.deletions;
        this.changes = changes != null ? changes : this.changes;
    }

    public void updatePatch(String patch) {
        this.patch = patch;
    }

    private static void validate(Long commitLogId, String filename, FileChangeStatus status) {
        if (commitLogId == null) {
            throw new IllegalArgumentException("Commit Log ID는 필수입니다.");
        }
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Filename은 비어 있을 수 없습니다.");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status는 필수입니다.");
        }
    }

    public enum FileChangeStatus {
        ADDED, MODIFIED, REMOVED, RENAMED
    }
}