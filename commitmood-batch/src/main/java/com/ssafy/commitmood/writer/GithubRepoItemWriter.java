package com.ssafy.commitmood.writer;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.mapper.GithubRepoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class GithubRepoItemWriter implements ItemWriter<GithubRepo> {

    private final GithubRepoMapper mapper;
    private final Long jobExecutionId;

    public GithubRepoItemWriter(GithubRepoMapper mapper, Long jobExecutionId) {
        this.mapper = mapper;
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public void write(Chunk<? extends GithubRepo> chunk) throws Exception {
        for (GithubRepo entity : chunk) {
            try {
                if (mapper.existsByGithubRepoId(entity.getGithubRepoId())) {
                    // 기존 데이터 업데이트
                    GithubRepo existing = mapper.findByGithubRepoId(entity.getGithubRepoId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "Repository exists but not found: " + entity.getGithubRepoId()));

                    existing.updateInfo(
                            entity.getGithubRepoName(),
                            entity.getDefaultBranch(),
                            entity.getDescription(),
                            entity.getGithubRepoUrl(),
                            entity.getIsPrivate()
                    );

                    mapper.update(existing);
                    log.info("Updated repository: {} (githubRepoId: {}, dbId: {})",
                            existing.getGithubRepoName(), existing.getGithubRepoId(), existing.getId());

                } else {
                    // 신규 데이터 삽입
                    mapper.insert(entity);
                    log.info("Inserted repository: {} (githubRepoId: {}, dbId: {})",
                            entity.getGithubRepoName(), entity.getGithubRepoId(), entity.getId());
                }

            } catch (Exception e) {
                log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=RepoSyncStep, " +
                                "failureType=DB_WRITE, entityType=REPOSITORY, entityId={}, " +
                                "retryCount=0, errorMessage={}",
                        jobExecutionId, entity.getGithubRepoId(), e.getMessage());
                throw e; // 청크 전체 롤백
            }
        }
    }
}
