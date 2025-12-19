package com.ssafy.commitmood.reader;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.mapper.GithubRepoMapper;
import com.ssafy.commitmood.dto.GithubCommitDto;
import com.ssafy.commitmood.dto.GithubCommitStatsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Slf4j
public class GithubCommitItemReader implements ItemReader<GithubCommitDto> {

    private final GithubReader githubReader;
    private final GithubRepoMapper githubRepoMapper;
    private final Long jobExecutionId;

    private Queue<GithubCommitDto> commitQueue;
    private List<GithubRepo> repositories;
    private Map<Long, Long> repoIdMap; // githubRepoId -> dbId
    private int currentRepoIndex;
    private int currentPage;
    private boolean hasMoreCommits;
    private GithubRepo currentRepo;
    private static final int PER_PAGE = 100;

    public GithubCommitItemReader(
            GithubReader githubReader,
            GithubRepoMapper githubRepoMapper,
            Long jobExecutionId
    ) {
        this.githubReader = githubReader;
        this.githubRepoMapper = githubRepoMapper;
        this.jobExecutionId = jobExecutionId;
        this.commitQueue = new LinkedList<>();
        this.currentRepoIndex = 0;
        this.currentPage = 1;
        this.hasMoreCommits = true;

        // DB에서 모든 Repository 조회
        this.repositories = githubRepoMapper.findAll();

        // repoIdMap 생성
        this.repoIdMap = new HashMap<>();
        for (GithubRepo repo : repositories) {
            repoIdMap.put(repo.getGithubRepoId(), repo.getId());
        }

        log.info("Initialized GithubCommitItemReader with {} repositories", repositories.size());
        log.info("Loaded {} repository ID mappings from database", repoIdMap.size());
    }

    @Override
    public GithubCommitDto read() throws Exception {
        if (commitQueue.isEmpty()) {
            fetchNextCommits();
        }

        if (!commitQueue.isEmpty()) {
            return commitQueue.poll();
        }

        return null; // Step 종료
    }

    private void fetchNextCommits() {
        while (commitQueue.isEmpty() && currentRepoIndex < repositories.size()) {
            currentRepo = repositories.get(currentRepoIndex);

            String[] parts = currentRepo.getGithubRepoFullName().split("/");
            if (parts.length != 2) {
                log.warn("Invalid repository full name format: {}", currentRepo.getGithubRepoFullName());
                moveToNextRepo();
                continue;
            }

            String username = parts[0];
            String repoName = parts[1];

            try {
                List<GithubCommitDto> commits = callWithRetry(
                        () -> githubReader.callGithubRepoToCommit(username, repoName, currentPage, PER_PAGE),
                        "callGithubRepoToCommit",
                        currentRepo.getGithubRepoId().toString()
                );

                log.info("Fetched {} commits from {}/{} (page {})",
                        commits.size(), username, repoName, currentPage);

                if (commits.size() < PER_PAGE) {
                    hasMoreCommits = false;
                }

                for (GithubCommitDto commit : commits) {
                    try {
                        enrichCommitWithStats(commit, username, repoName);

                        // githubRepoId와 dbRepoId 설정
                        commit.setGithubRepoId(currentRepo.getGithubRepoId());
                        commit.setDbRepoId(repoIdMap.get(currentRepo.getGithubRepoId()));

                        commitQueue.add(commit);
                    } catch (Exception e) {
                        log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                                        "failureType=API_CALL, entityType=COMMIT_STATS, entityId={}, " +
                                        "githubRepoId={}, page={}, retryCount=3, errorMessage={}",
                                jobExecutionId, commit.getSha(), currentRepo.getGithubRepoId(),
                                currentPage, e.getMessage());
                        // 해당 commit 스킵
                    }
                }

                currentPage++;

                if (!hasMoreCommits) {
                    moveToNextRepo();
                }

            } catch (Exception e) {
                log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=CommitSyncStep, " +
                                "failureType=API_CALL, entityType=REPOSITORY_COMMITS, " +
                                "entityId={}, page={}, retryCount=3, errorMessage={}",
                        jobExecutionId, currentRepo.getGithubRepoId(), currentPage, e.getMessage());

                // 해당 Repository 전체 스킵
                moveToNextRepo();
            }
        }
    }

    private void enrichCommitWithStats(GithubCommitDto commit, String username, String repoName) throws Exception {
        GithubCommitStatsDto stats = callWithRetry(
                () -> githubReader.callGithubRepoToCommitStats(username, repoName, commit.getSha(), 1, 1),
                "callGithubRepoToCommitStats",
                commit.getSha()
        );

        GithubCommitDto.StatsDto statsDto = new GithubCommitDto.StatsDto();
        statsDto.setAdditions(stats.getStats().getAdditions());
        statsDto.setDeletions(stats.getStats().getDeletions());
        statsDto.setTotal(stats.getStats().getTotal());
        statsDto.setFilesChanged((long) stats.getFiles().size());

        commit.setStats(statsDto);
    }

    private void moveToNextRepo() {
        currentRepoIndex++;
        currentPage = 1;
        hasMoreCommits = true;
    }

    private <T> T callWithRetry(ApiCall<T> apiCall, String apiName, String entityId) throws Exception {
        int retryCount = 0;
        Exception lastException = null;

        while (retryCount < 3) {
            try {
                return apiCall.call();
            } catch (Exception e) {
                lastException = e;
                retryCount++;
                if (retryCount < 3) {
                    log.info("API call failed for {}, retrying... (attempt {}/3): {}",
                            apiName, retryCount, e.getMessage());
                    Thread.sleep((long) (1000 * Math.pow(2, retryCount - 1))); // 1초의 2의 배수꼴로 대기
                }
            }
        }

        throw lastException;
    }

    @FunctionalInterface
    private interface ApiCall<T> {
        T call() throws Exception;
    }
}