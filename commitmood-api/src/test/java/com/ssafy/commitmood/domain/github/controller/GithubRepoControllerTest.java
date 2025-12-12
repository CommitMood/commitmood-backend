package com.ssafy.commitmood.domain.github.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubCommitResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoListResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoResponse;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.service.GithubRepoQueryService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class GithubRepoControllerTest {

    MockMvc mockMvc;

    @Mock
    GithubRepoQueryService service;

    @InjectMocks
    GithubRepoController controller;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // sample DTO
    private GithubRepoResponse sample(long id, long userId) {
        return new GithubRepoResponse(
                id, userId, 1000L + id,
                "repo-" + id,
                "devys/repo-" + id,
                "main",
                "desc" + id,
                "https://github.com/devys/repo-" + id,
                false,
                LocalDateTime.now()
        );
    }

    // =====================================================
    // 1. Repo 단건 조회
    // =====================================================
    @Test
    @DisplayName("GET /repos/{repoId} → 단건 조회 성공")
    void getRepo_success() throws Exception {
        long repoId = 2L;
        GithubRepoResponse res = sample(repoId, 11L);

        given(service.getRepo(repoId)).willReturn(res);

        mockMvc.perform(get("/repos/{repoId}", repoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(repoId))
                .andExpect(jsonPath("$.userAccountId").value(11L))
                .andExpect(jsonPath("$.name").value("repo-2"))
                .andExpect(jsonPath("$.isPrivate").value(false));

        then(service).should().getRepo(repoId);
    }

    // =====================================================
    // 2. 특정 사용자 Repo 목록 조회
    // =====================================================
    @Test
    @DisplayName("GET /users/{userId}/repos → 목록 조회 OK")
    void getUserRepos_success() throws Exception {
        long userId = 7L;

        GithubRepo r1 = GithubRepo.create(
                userId, 2001L, "repo-1", "devys/repo-1",
                "main", "desc1", "https://github.com/devys/repo-1", false
        );
        GithubRepo r2 = GithubRepo.create(
                userId, 2002L, "repo-2", "devys/repo-2",
                "main", "desc2", "https://github.com/devys/repo-2", false
        );

        GithubRepoListResponse list = GithubRepoListResponse.of(List.of(r1, r2));

        given(service.getUserRepos(userId)).willReturn(list);

        mockMvc.perform(get("/users/{userId}/repos", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repoList.length()").value(2))
                .andExpect(jsonPath("$.repoList[0].userAccountId").value(userId));

        then(service).should().getUserRepos(userId);
    }

    // =====================================================
    // 3-A. 검색 기본 (비페이징 → ListResponse)
    // =====================================================
    @Test
    @DisplayName("GET /repos/search?keyword=xx → List 검색 성공")
    void search_basic_success() throws Exception {
        String keyword = "commit";

        GithubRepo repo = GithubRepo.create(
                10L,
                1003L,
                "repo-3",
                "devys/repo-3",
                "main",
                "desc3",
                "https://github.com/devys/repo-3",
                false
        );

        GithubRepoListResponse list = GithubRepoListResponse.of(List.of(repo));

        given(service.search(keyword)).willReturn(list);

        mockMvc.perform(get("/repos/search").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repoList.length()").value(1))
                .andExpect(jsonPath("$.repoList[0].name").value("repo-3"));

        then(service).should().search(keyword);
    }

    // =====================================================
    // 3-B. 검색 + 페이징 (PageResponse)
    // =====================================================
    @Test
    @DisplayName("GET /repos/search/page?keyword=xx&page=1&size=10 → PageResponse 성공")
    void search_paged_success() throws Exception {
        String keyword = "commit";

        PageResponse<GithubRepoResponse> response = PageResponse.of(
                List.of(sample(4L, 20L)),
                1,
                10,
                30L
        );

        given(service.searchPaged(keyword, 1, 10)).willReturn(response);

        mockMvc.perform(get("/repos/search/page")
                        .param("keyword", keyword)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalCount").value(30))
                .andExpect(jsonPath("$.totalPages").value(3));

        then(service).should().searchPaged(keyword, 1, 10);
    }

    // =====================================================
    // 4. Commit 조회 (미구현 → 호출 체크만)
    // =====================================================
    @Test
    @DisplayName("GET /repos/{repoId}/commits → 호출만 확인")
    void commits_call_success() throws Exception {
        long repoId = 9L;

        mockMvc.perform(get("/repos/{repoId}/commits", repoId))
                .andExpect(status().isOk());

        then(service).should().getCommitsByRepo(repoId);
    }

    // =====================================================
    // 5. Commit 조회 (실제 구현 버전) - CommitResponse 목록 검증
    // =====================================================
    @Test
    @DisplayName("GET /repos/{repoId}/commits → Commit 목록 조회 성공")
    void getCommitsByRepo_success() throws Exception {
        long repoId = 5L;

        GithubCommitResponse c1 = new GithubCommitResponse(
                1L,
                repoId,
                10L,
                "a".repeat(40),
                "feat: initial commit",
                LocalDateTime.of(2025, 1, 1, 10, 0),
                5,
                0,
                5
        );

        GithubCommitResponse c2 = new GithubCommitResponse(
                2L,
                repoId,
                10L,
                "b".repeat(40),
                "fix: apply hotfix",
                LocalDateTime.of(2025, 1, 2, 12, 30),
                3,
                1,
                4
        );

        // mock service return
        given(service.getCommitsByRepo(repoId))
                .willReturn(List.of(c1, c2));

        mockMvc.perform(get("/repos/{repoId}/commits", repoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].repoId").value(repoId))
                .andExpect(jsonPath("$[0].sha").value("a".repeat(40)))
                .andExpect(jsonPath("$[0].message").value("feat: initial commit"))
                .andExpect(jsonPath("$[0].additions").value(5))
                .andExpect(jsonPath("$[0].totalChanges").value(5))
                .andExpect(jsonPath("$[1].sha").value("b".repeat(40)))
                .andExpect(jsonPath("$[1].totalChanges").value(4));

        then(service).should().getCommitsByRepo(repoId);
    }
}