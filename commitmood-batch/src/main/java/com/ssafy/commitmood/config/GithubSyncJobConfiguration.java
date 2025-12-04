package com.ssafy.commitmood.config;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.mapper.CommitLogMapper;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.mapper.GithubRepoMapper;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import com.ssafy.commitmood.dto.GithubCommitDto;
import com.ssafy.commitmood.dto.GithubCommitDtoMapper;
import com.ssafy.commitmood.dto.GithubRepoDto;
import com.ssafy.commitmood.dto.GithubRepoDtoMapper;
import com.ssafy.commitmood.processor.GithubCommitItemProcessor;
import com.ssafy.commitmood.processor.GithubRepoItemProcessor;
import com.ssafy.commitmood.reader.GithubCommitItemReader;
import com.ssafy.commitmood.reader.GithubReader;
import com.ssafy.commitmood.reader.GithubRepoItemReader;
import com.ssafy.commitmood.writer.CommitLogItemWriter;
import com.ssafy.commitmood.writer.GithubRepoItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GithubSyncJobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final GithubReader githubReader;
    private final GithubRepoMapper githubRepoMapper;
    private final CommitLogMapper commitLogMapper;
    private final GithubRepoDtoMapper githubRepoDtoMapper;
    private final GithubCommitDtoMapper githubCommitDtoMapper;

    private static final int CHUNK_SIZE = 10;

    @Bean
    public Job githubSyncJob() {
        return new JobBuilder("githubSyncJob", jobRepository)
                .start(repoSyncStep())
                .next(commitSyncStep())
                .build();
    }

    @Bean
    public Step repoSyncStep() {
        return new StepBuilder("repoSyncStep", jobRepository)
                .<GithubRepoDto, GithubRepo>chunk(CHUNK_SIZE, transactionManager)
                .reader(githubRepoItemReader(null, null))
                .processor(githubRepoItemProcessor(null, null))
                .writer(githubRepoItemWriter(null))
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(3)
                .build();
    }

    @Bean
    public Step commitSyncStep() {
        return new StepBuilder("commitSyncStep", jobRepository)
                .<GithubCommitDto, CommitLog>chunk(CHUNK_SIZE, transactionManager)
                .reader(githubCommitItemReader(null))
                .processor(githubCommitItemProcessor(null))
                .writer(commitLogItemWriter(null))
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(3)
                .build();
    }

    @Bean
    @StepScope
    public GithubRepoItemReader githubRepoItemReader(
            @Value("#{jobParameters['username']}") String username,
            @Value("#{stepExecution.jobExecutionId}") Long jobExecutionId
    ) {
        // username이 없으면 기본값 사용
        String targetUsername = username != null ? username : "swkim12345";

        log.info("Creating GithubRepoItemReader for user: {}, jobExecutionId: {}",
                targetUsername, jobExecutionId);

        return new GithubRepoItemReader(githubReader, targetUsername, jobExecutionId);
    }

    @Bean
    @StepScope
    public GithubRepoItemProcessor githubRepoItemProcessor(
            @Value("#{stepExecution.jobExecutionId}") Long jobExecutionId,
            UserAccountMapper userAccountMapper) {
        return new GithubRepoItemProcessor(userAccountMapper, githubRepoDtoMapper, jobExecutionId);
    }

    @Bean
    @StepScope
    public GithubRepoItemWriter githubRepoItemWriter(
            @Value("#{stepExecution.jobExecutionId}") Long jobExecutionId
    ) {
        return new GithubRepoItemWriter(githubRepoMapper, jobExecutionId);
    }

    @Bean
    @StepScope
    public GithubCommitItemReader githubCommitItemReader(
            @Value("#{stepExecution.jobExecutionId}") Long jobExecutionId
    ) {
        return new GithubCommitItemReader(githubReader, githubRepoMapper, jobExecutionId);
    }

    @Bean
    @StepScope
    public GithubCommitItemProcessor githubCommitItemProcessor(
            @Value("#{stepExecution.jobExecutionId}") Long jobExecutionId
    ) {
        return new GithubCommitItemProcessor(githubCommitDtoMapper, jobExecutionId);
    }

    @Bean
    @StepScope
    public CommitLogItemWriter commitLogItemWriter(
            @Value("#{stepExecution.jobExecutionId}") Long jobExecutionId
    ) {
        return new CommitLogItemWriter(commitLogMapper, jobExecutionId);
    }
}