package com.ssafy.commitmood.scheduler;
import com.ssafy.commitmood.dto.GithubCommitDto;
import com.ssafy.commitmood.dto.GithubRepoDto;
import com.ssafy.commitmood.reader.GithubReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GithubReaderScheduler {
    private final GithubReader reader;
    private final JobLauncher jobLauncher;
    private final Job githubSyncJob;

//    @Scheduled(cron = "* * * * * *")
    public void runUserToRepo() {
        List<GithubRepoDto> result = reader.callGithubUserToRepo("swkim12345", 1, 1);

        for (var dto : result) {
            log.warn("result : {}", dto);
        }
    }

//    @Scheduled(cron = "* * * * * *")
    public void runRepoToCommit() {
        List<GithubCommitDto> result = reader.callGithubRepoToCommit("swkim12345", "algo_practice", 1, 1);

        for (var dto : result) {
            var sha = dto.getSha();
            var commitStatsDto = reader.callGithubRepoToCommitStats("swkim12345","algo_practice", sha, 1, 1);
            GithubCommitDto.StatsDto statsDto = new  GithubCommitDto.StatsDto();
            statsDto.setAdditions(commitStatsDto.getStats().getAdditions());
            statsDto.setDeletions(commitStatsDto.getStats().getDeletions());
            statsDto.setTotal(commitStatsDto.getStats().getTotal());
            statsDto.setFilesChanged((long) commitStatsDto.getFiles().size());
            dto.setStats(statsDto);

            log.warn("result : {}", dto);
        }
    }

    @Scheduled(cron = "0 0 2 * * ?") // 매일 새벽 2시
    public void runGithubSync() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addString("username", "swkim12345")
                    .addLong("executionTime", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(githubSyncJob, params);
            log.info("Batch job completed with status: {}", execution.getStatus());
        } catch (Exception e) {
            log.error("Failed to run batch job", e);
        }
    }
}
