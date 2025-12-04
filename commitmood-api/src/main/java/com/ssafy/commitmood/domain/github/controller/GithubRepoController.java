package com.ssafy.commitmood.domain.github.controller;

import com.ssafy.commitmood.domain.github.dto.response.GithubRepoListResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoResponse;
import com.ssafy.commitmood.domain.github.service.GithubRepoQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GithubRepoController {

    private final GithubRepoQueryService githubRepoQueryService;

    /**
     * 1) 특정 사용자 Repo 목록 조회
     */
    @GetMapping("/users/{userId}/repos")
    public GithubRepoListResponse getUserRepos(@PathVariable Long userId) {
        return githubRepoQueryService.getUserRepos(userId);
    }

    /**
     * 2) Repo 단건 조회
     */
    @GetMapping("/repos/{repoId}")
    public GithubRepoResponse getRepo(@PathVariable Long repoId) {
        return githubRepoQueryService.getRepo(repoId);
    }

    /**
     * 3) keyword 검색 + 페이징 optional
     */
    @GetMapping("/repos/search")
    public Object searchRepos(
            @RequestParam String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        // 페이지 파라미터가 없다면 일반 검색
        if (page == null || size == null) {
            return githubRepoQueryService.search(keyword);
        }

        return githubRepoQueryService.searchPaged(keyword, page, size);
    }

    /**
     * 4) Commit 조회 (아직 미구현)
     */
    @GetMapping("/repos/{repoId}/commits")
    public Object getCommitsByRepo(@PathVariable Long repoId) {
        return githubRepoQueryService.getCommitsByRepo(repoId);
    }
}