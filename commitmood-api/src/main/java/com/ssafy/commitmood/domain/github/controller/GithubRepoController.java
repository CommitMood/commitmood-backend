package com.ssafy.commitmood.domain.github.controller;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubCommitResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoListResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoResponse;
import com.ssafy.commitmood.domain.github.service.GithubRepoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

    @Operation(summary = "특정 사용자의 GitHub Repo 리스트 조회",
            description = "userId 기반 Repo 리스트 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = GithubRepoListResponse.class))),
                    @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/users/{userId}/repos")
    public GithubRepoListResponse getUserRepos(@PathVariable Long userId) {
        return githubRepoQueryService.getUserRepos(userId);
    }

    @Operation(summary = "GitHub Repo 단건 조회",
            description = "repoId 기반 Repo 상세 조회",
            responses = {
                    @ApiResponse(responseCode = "200", description = "조회 성공",
                            content = @Content(schema = @Schema(implementation = GithubRepoResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Repo가 존재하지 않음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/repos/{repoId}")
    public GithubRepoResponse getRepo(@PathVariable Long repoId) {
        return githubRepoQueryService.getRepo(repoId);
    }

    @Operation(summary = "GitHub Repo 검색 (비페이징)",
            description = "keyword 기반 Repo 리스트 검색",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공",
                            content = @Content(schema = @Schema(implementation = GithubRepoListResponse.class))),
                    @ApiResponse(responseCode = "400", description = "keyword 파라미터 오류"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/repos/search")
    public GithubRepoListResponse searchRepos(@RequestParam String keyword) {
        return githubRepoQueryService.search(keyword);
    }

    @Operation(summary = "GitHub Repo 검색 (페이징)",
            description = "keyword + page/size 기반 검색",
            responses = {
                    @ApiResponse(responseCode = "200", description = "검색 성공",
                            content = @Content(schema = @Schema(implementation = PageResponse.class))),
                    @ApiResponse(responseCode = "400", description = "페이징 파라미터 오류"),
                    @ApiResponse(responseCode = "500", description = "서버 오류")
            }
    )
    @GetMapping("/repos/search/page")
    public PageResponse<GithubRepoResponse> searchReposPaged(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return githubRepoQueryService.searchPaged(keyword, page, size);
    }

    @Operation(
            summary = "GitHub Repo Commit 조회",
            description = "repoId 기반 Commit 목록 조회",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "조회 성공",
                            content = @Content(
                                    schema = @Schema(implementation = GithubCommitResponse.class)
                            )
                    ),
                    @ApiResponse(responseCode = "404", description = "Repo가 존재하지 않음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    @GetMapping("/repos/{repoId}/commits")
    public List<GithubCommitResponse> getCommitsByRepo(@PathVariable Long repoId) {
        return githubRepoQueryService.getCommitsByRepo(repoId);
    }
}