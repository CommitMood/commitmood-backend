package com.ssafy.commitmood.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BatchRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Job githubSyncJob;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Running batch job on application startup");

        JobParameters params = new JobParametersBuilder()
                .addString("username", "swkim12345")
                .addLong("executionTime", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncher.run(githubSyncJob, params);
        log.info("Batch job completed with status: {}", execution.getStatus());
    }
}