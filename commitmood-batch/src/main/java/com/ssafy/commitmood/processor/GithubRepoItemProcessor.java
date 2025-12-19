package com.ssafy.commitmood.processor;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import com.ssafy.commitmood.dto.GithubRepoDto;
import com.ssafy.commitmood.dto.GithubRepoDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GithubRepoItemProcessor implements ItemProcessor<GithubRepoDto, GithubRepo> {
    private final UserAccountMapper userAccountMapper;
    private final GithubRepoDtoMapper mapper;
    private final Long jobExecutionId;

    public GithubRepoItemProcessor( UserAccountMapper userAccountMapper,GithubRepoDtoMapper mapper, Long jobExecutionId) {
        this.userAccountMapper = userAccountMapper;
        this.mapper = mapper;
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public GithubRepo process(GithubRepoDto dto) throws Exception {
        if (dto == null) {
            log.warn("GithubRepoDto is null, skipping");
            return null;
        }

        try {
            UserAccount githubRepo = userAccountMapper.findByGithubUserId(dto.getOwner().getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "UserAccount not found for githubUserId: " + dto.getOwner().getId()
                    ));
            Long ownerId = githubRepo.getId();
            GithubRepo entity = mapper.toEntity(dto, ownerId);
            log.debug("Processed repository: {} ({})", entity.getGithubRepoName(), entity.getGithubRepoId());
            return entity;
        } catch (Exception e) {
            log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=RepoSyncStep, " +
                            "failureType=VALIDATION, entityType=REPOSITORY, entityId={}, " +
                            "retryCount=0, errorMessage={}",
                    jobExecutionId, dto.getId(), e.getMessage());
            return null; // 스킵
        }
    }
}