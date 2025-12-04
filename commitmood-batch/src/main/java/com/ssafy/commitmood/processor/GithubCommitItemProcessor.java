package com.ssafy.commitmood.processor;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.dto.GithubCommitDto;
import com.ssafy.commitmood.dto.GithubCommitDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GithubCommitItemProcessor implements ItemProcessor<GithubCommitDto, CommitLog> {

    private final GithubCommitDtoMapper mapper;
    private final Long jobExecutionId;

    public GithubCommitItemProcessor(GithubCommitDtoMapper mapper, Long jobExecutionId) {
        this.mapper = mapper;
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public CommitLog process(GithubCommitDto dto) throws Exception {
        if (dto == null) {
            log.warn("GithubCommitDto is null, skipping");
            return null;
        }

        // Author 검증
        if (dto.getAuthor() == null || dto.getAuthor().getId() == null) {
            log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                            "failureType=VALIDATION, entityType=COMMIT, entityId={}, " +
                            "retryCount=0, errorMessage=Author information is missing",
                    jobExecutionId, dto.getSha());
            return null;
        }

        Long authorId = dto.getAuthor().getId();

        // DTO 검증
        if (!mapper.validate(dto, authorId)) {
            log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                            "failureType=VALIDATION, entityType=COMMIT, entityId={}, " +
                            "retryCount=0, errorMessage=Author ID mismatch",
                    jobExecutionId, dto.getSha());
            return null;
        }

        // DB Repository ID 확인
        Long dbRepoId = dto.getDbRepoId();
        if (dbRepoId == null) {
            log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                            "failureType=VALIDATION, entityType=COMMIT, entityId={}, " +
                            "retryCount=0, errorMessage=Repository not found in database (githubRepoId: {})",
                    jobExecutionId, dto.getSha(), dto.getGithubRepoId());
            return null;
        }

        try {
            CommitLog entity = mapper.toEntity(dto, dbRepoId, authorId);
            log.debug("Processed commit: {} for repo {}", entity.getGithubCommitSha(), dbRepoId);
            return entity;
        } catch (Exception e) {
            log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                            "failureType=VALIDATION, entityType=COMMIT, entityId={}, " +
                            "retryCount=0, errorMessage={}",
                    jobExecutionId, dto.getSha(), e.getMessage());
            return null;
        }
    }
}