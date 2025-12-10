package com.ssafy.commitmood.domain.github.controller;

import com.ssafy.commitmood.domain.github.dto.response.GithubRepoListResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoResponse;
import com.ssafy.commitmood.domain.github.service.GithubRepoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GitHubRepo", description = "사용자 GitHub 저장소 조회 및 검색 API")
@RestController
@RequiredArgsConstructor
public class GithubRepoController {

    private final GithubRepoQueryService githubRepoQueryService;

    @Operation(
            summary = "특정 사용자의 GitHub Repo 리스트 조회",
            description = "userId 기반으로 GitHub Repo 리스트를 조회한다."
    )
    @GetMapping("/users/{userId}/repos")
    public GithubRepoListResponse getUserRepos(
            @Parameter(description = "내부 사용자 ID", example = "1") @PathVariable Long userId
    ) {
        return githubRepoQueryService.getUserRepos(userId);
    }

    @Operation(
            summary = "GitHub Repo 단건 조회",
            description = "repoId 기반으로 특정 GitHub Repo 정보를 조회한다."
    )
    @GetMapping("/repos/{repoId}")
    public GithubRepoResponse getRepo(
            @Parameter(description = "Repo ID", example = "130")
            @PathVariable Long repoId
    ) {
        return githubRepoQueryService.getRepo(repoId);
    }

    @Operation(
            summary = "GitHub Repo 검색",
            description = "keyword 기반 검색 / (page, size 성립 시 페이징) → 둘 다 없으면 일반 검색으로 처리됨."
    )
    @GetMapping("/repos/search")
    public Object searchRepos(
            @Parameter(description = "검색 keyword", example = "spring")
            @RequestParam String keyword,

            @Parameter(description = "페이지 번호(옵션)", example = "1")
            @RequestParam(required = false) Integer page,

            @Parameter(description = "페이지 사이즈(옵션)", example = "10")
            @RequestParam(required = false) Integer size
    ) {
        if (page == null || size == null) {
            return githubRepoQueryService.search(keyword);
        }
        return githubRepoQueryService.searchPaged(keyword, page, size);
    }

    @Operation(
            summary = "GitHub Repo Commit 조회 (미구현)",
            description = "repoId 기반 Commit 리스트 조회 API (예정)"
    )
    @GetMapping("/repos/{repoId}/commits")
    public Object getCommitsByRepo(
            @Parameter(description = "Repo ID", example = "130")
            @PathVariable Long repoId
    ) {
        return githubRepoQueryService.getCommitsByRepo(repoId);
    }
}