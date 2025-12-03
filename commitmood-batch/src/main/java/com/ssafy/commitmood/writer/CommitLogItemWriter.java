package com.ssafy.commitmood.writer;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.mapper.CommitLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class CommitLogItemWriter implements ItemWriter<CommitLog> {

    private final CommitLogMapper mapper;
    private final Long jobExecutionId;

    public CommitLogItemWriter(CommitLogMapper mapper, Long jobExecutionId) {
        this.mapper = mapper;
        this.jobExecutionId = jobExecutionId;
    }

    @Override
    public void write(Chunk<? extends CommitLog> chunk) throws Exception {
        for (CommitLog entity : chunk) {
            try {
                if (mapper.existsByRepoIdAndSha(
                        entity.getGithubRepoId(),
                        entity.getGithubCommitSha())) {

                    // 기존 데이터 업데이트
                    CommitLog existing = mapper.findByRepoIdAndSha(
                            entity.getGithubRepoId(),
                            entity.getGithubCommitSha()
                    ).orElseThrow(() -> new IllegalStateException(
                            "Commit exists but not found: " + entity.getGithubCommitSha()));

                    existing.updateStats(
                            entity.getAdditions(),
                            entity.getDeletions(),
                            entity.getTotalChanges(),
                            entity.getFilesChanged()
                    );
                    existing.updateMessage(entity.getMessage());

                    mapper.update(existing);
                    log.info("Updated commit: {} for repo {}",
                            existing.getGithubCommitSha(), existing.getGithubRepoId());

                } else {
                    // 신규 데이터 삽입
                    mapper.insert(entity);
                    log.info("Inserted commit: {} for repo {}",
                            entity.getGithubCommitSha(), entity.getGithubRepoId());
                }

            } catch (Exception e) {
                log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                                "failureType=DB_WRITE, entityType=COMMIT, entityId={}, " +
                                "retryCount=0, errorMessage={}",
                        jobExecutionId, entity.getGithubCommitSha(), e.getMessage());
                throw e; // 청크 전체 롤백
            }
        }
    }
}