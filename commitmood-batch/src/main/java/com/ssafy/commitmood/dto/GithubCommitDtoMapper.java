package com.ssafy.commitmood.dto;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class GithubCommitDtoMapper {

    public CommitLog toEntity(GithubCommitDto dto, Long repoId, Long authorId) {
        if (dto == null) {
            log.warn("dto is null");
            return null;
        }

        String githubCommitSha = dto.getSha();
        LocalDateTime committedAt = dto.getCommit().getAuthor().getDate();
        String message = dto.getCommit().getMessage();
        String htmlUrl = dto.getHtmlUrl();
        Long additions = dto.getStats().getAdditions();
        Long deletions = dto.getStats().getDeletions();
        Long totalChanges = dto.getStats().getTotal();
        Long filesChanged = dto.getStats().getFilesChanged();

        return CommitLog.create(repoId, authorId, githubCommitSha, committedAt
        ,message,htmlUrl,additions,deletions,totalChanges,filesChanged);
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
