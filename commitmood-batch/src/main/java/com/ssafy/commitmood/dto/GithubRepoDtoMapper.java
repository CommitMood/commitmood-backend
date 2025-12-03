package com.ssafy.commitmood.dto;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GithubRepoDtoMapper {
    public GithubRepo toEntity(GithubRepoDto dto, Long ownerId) {
        if (dto == null) {
            log.warn("GithubRepositoryDto is null, cannot convert to Repository entity.");
            return null;
        }

        Long githubRepoId = dto.getId();
        String githubRepoName = dto.getName();
        String githubRepoFullName = dto.getFullName();
        String defaultBranch = dto.getDefaultBranch();
        String description = dto.getDescription();
        String githubRepoUrl = dto.getHtmlUrl();
        Boolean isPrivate = dto.isPrivate();

        return GithubRepo.create(ownerId, githubRepoId, githubRepoName, githubRepoFullName, defaultBranch, description, githubRepoUrl, isPrivate);
    }

    /**
     * 기존 ownerId와 DTO의 ownerId가 일치하는지 검증
     * @param dto api 응답 DTO
     * @param ownerId 검증할 ownerId
     * @return 일치(true), 불일치(false)
     */
    public boolean validate(GithubRepoDto dto, Long ownerId) {
        if (dto == null || dto.getOwner() == null) {
            log.warn("GithubRepositoryDto is null, cannot validate owner.");
            return false;
        } else if (dto.getOwner().getId() == null || dto.getOwner().getLogin() == null) {
            log.warn("Owner information is incomplete in GithubRepositoryDto: {}", dto.getOwner());
            return false;
        }
        boolean isValid = dto.getOwner().getId().equals(ownerId);
        if (!isValid) {
            log.warn("Owner ID mismatch: expected {}, but got {}.",
                    ownerId, dto.getOwner().getId());
        }
        return isValid;
    }
}
