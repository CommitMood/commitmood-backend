package com.ssafy.commitmood.domain.github.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.commitmood.common.dto.response.PageResponse;
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

    ObjectMapper om = new ObjectMapper();

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // 샘플 DTO 생성
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

    // =====================================
    // 1. Repo 단건 조회
    // =====================================
    @Test
    @DisplayName("GET /api/repos/{repoId} → 단건 조회 성공")
    void getRepo_success() throws Exception {
        long repoId = 2L;
        GithubRepoResponse res = sample(repoId, 11L);

        given(service.getRepo(repoId)).willReturn(res);

        mockMvc.perform(get("/api/repos/{repoId}", repoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(repoId))
                .andExpect(jsonPath("$.userAccountId").value(11L))
                .andExpect(jsonPath("$.name").value("repo-2"))
                .andExpect(jsonPath("$.isPrivate").value(false));

        then(service).should().getRepo(repoId);
    }

    // =====================================
    // 2. 특정 사용자 Repo 목록 조회
    // =====================================
    @Test
    @DisplayName("GET /api/users/{userId}/repos → 목록 조회 OK")
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

        mockMvc.perform(get("/api/users/{userId}/repos", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repoList.length()").value(2))
                .andExpect(jsonPath("$.repoList[0].userAccountId").value(userId));

        then(service).should().getUserRepos(userId);
    }

    // =====================================
    // 3_A. 검색 기본 (page 없음 → ListResponse)
    // =====================================
    @Test
    @DisplayName("GET /api/repos/search?keyword=xx → List 검색 성공")
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

        mockMvc.perform(get("/api/repos/search").param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.repoList.length()").value(1))
                .andExpect(jsonPath("$.repoList[0].name").value("repo-3"));

        then(service).should().search(keyword);
    }

    // =====================================
    // 3_B. 검색 + 페이징 (page,size 포함 → PageResponse)
    // =====================================
    @Test
    @DisplayName("GET /api/repos/search?page=&size= → PageResponse 성공")
    void search_paged_success() throws Exception {
        String keyword = "commit";

        PageResponse<GithubRepoResponse> response = PageResponse.of(
                List.of(sample(4L, 20L)),
                1,
                10,
                30L
        );

        given(service.searchPaged(keyword, 1, 10)).willReturn(response);

        mockMvc.perform(get("/api/repos/search")
                        .param("keyword", keyword)
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalPages").value(3));

        then(service).should().searchPaged(keyword, 1, 10);
    }

    // =====================================
    // 4. TODO Commit 조회 (현재는 호출만 확인)
    // =====================================
    @Test
    @DisplayName("GET /api/repos/{id}/commits → 미구현 호출 확인")
    void commits_call_success() throws Exception {
        long repoId = 9L;
        mockMvc.perform(get("/api/repos/{repoId}/commits", repoId))
                .andExpect(status().isOk()); // 현재 UnsupportedOperationException → 응답 200 보장 X

        then(service).should().getCommitsByRepo(repoId);
    }
}