package com.ssafy.commitmood.domain.github.dto.response;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "GitHub Repo 커밋 조회 응답 DTO")
public record GithubCommitResponse(
        @Schema(description = "커밋 PK ID", example = "1")
        Long id,

        @Schema(description = "GitHub Repo ID", example = "10")
        Long repoId,

        @Schema(description = "커밋 작성자 User ID", example = "5")
        Long userAccountId,

        @Schema(description = "GitHub Commit SHA", example = "2b3010ecff76...")
        String sha,

        @Schema(description = "커밋 메시지", example = "fix: handle null pointer in ranking api")
        String message,

        @Schema(description = "커밋 시각", example = "2025-12-01T10:15:30")
        LocalDateTime committedAt,

        @Schema(description = "추가된 코드 라인 수", example = "10")
        Integer additions,

        @Schema(description = "삭제된 코드 라인 수", example = "2")
        Integer deletions,
        
        @Schema(description = "전체 변경 라인 수", example = "12")
        Integer totalChanges
) {

    public static GithubCommitResponse of(CommitLog log) {
        return new GithubCommitResponse(
                log.getId(),
                log.getGithubRepoId(),
                log.getUserAccountId(),
                log.getGithubCommitSha(),
                log.getMessage(),
                log.getCommittedAt(),
                log.getAdditions() != null ? log.getAdditions().intValue() : null,
                log.getDeletions() != null ? log.getDeletions().intValue() : null,
                log.getTotalChanges() != null ? log.getTotalChanges().intValue() : null
        );
    }
}