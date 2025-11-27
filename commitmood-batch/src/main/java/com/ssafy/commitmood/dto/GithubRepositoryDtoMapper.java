package com.ssafy.commitmood.dto;

import com.ssafy.commitmood.domain.Repository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class GithubRepositoryDtoMapper {
    public Repository toEntity(GithubRepositoryDto dto, Long ownerId) {
        if (dto == null) {
            log.warn("GithubRepositoryDto is null, cannot convert to Repository entity.");
            return null;
        }

        return Repository.builder()
                .ownerId(dto.getOwner().getId())
                .githubRepoId(dto.getId())
                .name(dto.getName())
                .fullName(dto.getFullName())
                .defaultBranch(dto.getDefaultBranch())
                .htmlUrl(dto.getHtmlUrl())
                .isPrivate(dto.isPrivate())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }

    public void updateEntity(Repository existing, GithubRepositoryDto dto) {
        if (existing == null || dto == null) {
            log.warn("Cannot update: existing={}, dto={}", existing, dto);
            return;
        }

        existing.setGithubRepoId(dto.getId());
        existing.setName(dto.getName());
        existing.setFullName(dto.getFullName());
        existing.setDefaultBranch(dto.getDefaultBranch());
        existing.setHtmlUrl(dto.getHtmlUrl());
        existing.setIsPrivate(dto.isPrivate());
        existing.setCreatedAt(dto.getCreatedAt());
        existing.setUpdatedAt(dto.getUpdatedAt());
        existing.setLastSyncedAt(LocalDateTime.now());
    }

    /**
     * 기존 ownerId와 DTO의 ownerId가 일치하는지 검증
     * @param dto api 응답 DTO
     * @param ownerId 검증할 ownerId
     * @return 일치(true), 불일치(false)
     */
    public boolean validate(GithubRepositoryDto dto, Long ownerId) {
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
