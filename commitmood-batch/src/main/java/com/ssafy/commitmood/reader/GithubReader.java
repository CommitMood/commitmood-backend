package com.ssafy.commitmood.reader;

import com.ssafy.commitmood.config.GithubRestClientConfig;
import com.ssafy.commitmood.dto.GithubCommitDto;
import com.ssafy.commitmood.dto.GithubCommitStatsDto;
import com.ssafy.commitmood.dto.GithubRepoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GithubReader {
    private final GithubRestClientConfig config;

    public List<GithubRepoDto> callGithubUserToRepo(String username, int page) {
        return callGithubUserToRepo(username, page, 100);
    }

    public List<GithubRepoDto> callGithubUserToRepo(String username, int page, int perPage) {
        return config.restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/users/{username}/repos")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build(username))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<GithubCommitDto> callGithubRepoToCommit(String username, String repo, int page, int perPage) {
        return config.restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/{username}/{repo}/commits")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build(username, repo))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public GithubCommitStatsDto callGithubRepoToCommitStats(String username, String repo, String ref, int page, int perPage) {
        return config.restClient().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/repos/{username}/{repo}/commits/{ref}")
                        .queryParam("page", page)
                        .queryParam("per_page", perPage)
                        .build(username, repo, ref))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
