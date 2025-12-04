package com.ssafy.commitmood.domain.github.dto.response;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import java.util.List;

public record GithubRepoListResponse(
        List<GithubRepoResponse> repoList,
        int count
) {

    public static GithubRepoListResponse of(List<GithubRepo> list) {
        return new GithubRepoListResponse(
                list.stream()
                        .map(GithubRepoResponse::of)
                        .toList(),
                list.size()
        );
    }
}
