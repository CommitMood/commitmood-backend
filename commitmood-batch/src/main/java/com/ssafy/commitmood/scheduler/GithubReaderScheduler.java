package com.ssafy.commitmood.scheduler;
import com.ssafy.commitmood.dto.GithubCommitDto;
import com.ssafy.commitmood.dto.GithubRepositoryDto;
import com.ssafy.commitmood.reader.GithubReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GithubReaderScheduler {
    private final GithubReader reader;

//    @Scheduled(cron = "* * * * * *")
    public void runUserToRepo() {
        List<GithubRepositoryDto> result = reader.callGithubUserToRepo("swkim12345", 1, 1);

        for (var dto : result) {
            log.warn("result : {}", dto);
        }
    }

    @Scheduled(cron = "* * * * * *")
    public void runRepoToCommit() {
        List<GithubCommitDto> result = reader.callGithubRepoToCommit("swkim12345", "algo_practice", 1, 1);

        for (var dto : result) {
            log.warn("result : {}", dto);
        }
    }
}
