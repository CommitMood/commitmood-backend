package com.ssafy.commitmood.reader;

import com.ssafy.commitmood.dto.GithubRepoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Slf4j
public class GithubRepoItemReader implements ItemReader<GithubRepoDto> {

    private final GithubReader githubReader;
    private final String username;
    private final Long jobExecutionId;

    private Queue<GithubRepoDto> repoQueue;
    private int currentPage;
    private boolean hasMore;
    private static final int PER_PAGE = 100;

    public GithubRepoItemReader(
            GithubReader githubReader,
            String username,
            Long jobExecutionId
    ) {
        this.githubReader = githubReader;
        this.username = username;
        this.jobExecutionId = jobExecutionId;
        this.repoQueue = new LinkedList<>();
        this.currentPage = 1;
        this.hasMore = true;
    }

    @Override
    public GithubRepoDto read() throws Exception {
        if (repoQueue.isEmpty() && hasMore) {
            fetchNextPage();
        }

        if (!repoQueue.isEmpty()) {
            return repoQueue.poll();
        }

        return null; // Step 종료
    }

    private void fetchNextPage() {
        try {
            List<GithubRepoDto> repos = callWithRetry(
                    () -> githubReader.callGithubUserToRepo(username, currentPage, PER_PAGE),
                    "callGithubUserToRepo",
                    "page:" + currentPage
            );

            log.info("Fetched {} repositories from page {}", repos.size(), currentPage);

            // 만약 더이상 없을 경우 -> size가 PER_PAGE보다 작음
            if (repos.size() < PER_PAGE) {
                hasMore = false;
                log.info("No more repositories to fetch for user: {}", username);
            }

            repoQueue.addAll(repos);
            currentPage++;

        } catch (Exception e) {
            log.warn("BATCH_FAILURE: jobExecutionId={}, stepName=RepoSyncStep, " +
                            "failureType=API_CALL, entityType=REPOSITORY, entityId={}, " +
                            "page={}, retryCount=3, errorMessage={}",
                    jobExecutionId, username, currentPage, e.getMessage());
            hasMore = false; // 실패 시 해당 사용자 스킵
        }
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
                    Thread.sleep((long) (1000 * Math.pow(2, retryCount - 1))); // 1초의 2의 배수 꼴로 대기
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