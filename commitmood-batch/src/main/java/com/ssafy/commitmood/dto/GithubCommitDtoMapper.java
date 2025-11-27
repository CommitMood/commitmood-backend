package com.ssafy.commitmood.dto;

import com.ssafy.commitmood.domain.CommitLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GithubCommitDtoMapper {

    public CommitLog toEntity(GithubCommitDto dto, Long repoId, Long authorId) {
        if (dto == null) {
            log.warn("dto is null");
            return null;
        }

        return CommitLog.builder()
                .repoId(repoId)
                .authorId(authorId)
                .githubCommitSha(dto.getSha())
                .committedAt(dto.getCommit().getAuthor().getDate())
                .message(dto.getCommit().getMessage())
                .htmlUrl(dto.getHtmlUrl())
                .additions(dto.getStats().getAdditions())
                .deletions(dto.getStats().getDeletions())
                .totalChanges(dto.getStats().getTotal())
                .filesChanged(dto.getStats().getFilesChanged())
                .build();
    }

    public void updateEntity(CommitLog existing, GithubCommitDto dto, Long repoId, Long authorId) {
        if (existing == null || dto == null) {
            log.warn("Cannot update: existing={}, dto={}", existing, dto);
            return;
        }

        existing.setCommittedAt(dto.getCommit().getAuthor().getDate());
        existing.setMessage(dto.getCommit().getMessage());
        existing.setHtmlUrl(dto.getHtmlUrl());
        existing.setAdditions(dto.getStats().getAdditions());
        existing.setDeletions(dto.getStats().getDeletions());
        existing.setTotalChanges(dto.getStats().getTotal());
        existing.setFilesChanged(dto.getStats().getFilesChanged());
    }

    /**
     * 기존 authorId와 DTO가 일치하는 지 검증
     * @param dto api 응답 dto
     * @param authorId 검증할 authorId
     * @return 일치(true), 불일치(false)
     */
    public boolean validate(GithubCommitDto dto, Long authorId) {
        if (dto == null || dto.getCommit() == null) {
            log.warn("GithubCommitDto is null, cannot validate.");
            return false;
        } else if (!dto.getAuthor().getId().equals(authorId)) {
            log.warn("GithubCommitDto's author id does not match.");
            return false;
        }
        return true;
    }
}
