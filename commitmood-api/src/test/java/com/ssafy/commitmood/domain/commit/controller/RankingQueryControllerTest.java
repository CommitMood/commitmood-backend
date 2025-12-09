package com.ssafy.commitmood.domain.commit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.service.RankingQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RankingQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ObjectMapper.class)
class RankingQueryControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankingQueryService rankingQueryService;

    private final String prefix = "";

    @Test
    @DisplayName("GET /{prefix}/rankings - 커밋 수 기준 랭킹 조회")
    void getRankings_CommitCount() throws Exception {
        List<RankingListResponse> rankings = List.of(
                RankingListResponse.builder()
                        .userAccountId(1L)
                        .githubLogin("user1")
                        .githubAvatarUrl("https://avatar1.url")
                        .commitCount(10)
                        .flaggedCount(5)
                        .swearCount(2)
                        .sentimentScore(0.5)
                        .recentCommitCount(3)
                        .build(),
                RankingListResponse.builder()
                        .userAccountId(2L)
                        .githubLogin("user2")
                        .githubAvatarUrl("https://avatar2.url")
                        .commitCount(8)
                        .flaggedCount(3)
                        .swearCount(1)
                        .sentimentScore(0.3)
                        .recentCommitCount(2)
                        .build()
        );

        PageResponse<RankingListResponse> response = PageResponse.of(rankings, 1, 30, 100);

        given(rankingQueryService.getRankings("commit_count", 1, 30))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/rankings")
                        .param("option", "commit_count")
                        .param("page", "1")
                        .param("per_page", "30"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].userAccountId").value(1))
                .andExpect(jsonPath("$.content[0].githubLogin").value("user1"))
                .andExpect(jsonPath("$.content[0].commitCount").value(10))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(30))
                .andExpect(jsonPath("$.totalCount").value(100));
    }

    @Test
    @DisplayName("GET /{prefix}/rankings - 플래그 토큰 수 기준 랭킹 조회")
    void getRankings_FlaggedCount() throws Exception {
        PageResponse<RankingListResponse> response = PageResponse.of(List.of(), 1, 30, 0);

        given(rankingQueryService.getRankings("flagged_count", 1, 30))
                .willReturn(response);
        
        var result = mockMvc.perform(get(prefix + "/rankings")
                        .param("option", "flagged_count"));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /{prefix}/rankings - 욕설 수 기준 랭킹 조회")
    void getRankings_SwearCount() throws Exception {
        PageResponse<RankingListResponse> response = PageResponse.of(List.of(), 1, 30, 0);

        given(rankingQueryService.getRankings("swear_count", 1, 30))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/rankings")
                        .param("option", "swear_count"));

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /{prefix}/rankings - 감정 점수 기준 랭킹 조회")
    void getRankings_SentimentScore() throws Exception {
        PageResponse<RankingListResponse> response = PageResponse.of(List.of(), 1, 30, 0);

        given(rankingQueryService.getRankings("sentiment_score", 1, 30))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/rankings")
                        .param("option", "sentiment_score"));

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /{prefix}/rankings - 최근 활동 기준 랭킹 조회")
    void getRankings_Recent() throws Exception {
        PageResponse<RankingListResponse> response = PageResponse.of(List.of(), 1, 30, 0);

        given(rankingQueryService.getRankings("recent", 1, 30))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/rankings")
                        .param("option", "recent"));

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /{prefix}/rankings - 파라미터 없이 조회 (기본값 적용)")
    void getRankings_DefaultParameters() throws Exception {
        PageResponse<RankingListResponse> response = PageResponse.of(List.of(), 1, 30, 0);

        given(rankingQueryService.getRankings("commit_count", 1, 30))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/rankings"));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /{prefix}/rankings - 페이징 처리")
    void getRankings_Paging() throws Exception {
        PageResponse<RankingListResponse> response = PageResponse.of(List.of(), 2, 10, 100);

        given(rankingQueryService.getRankings("commit_count", 2, 10))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/rankings")
                        .param("page", "2")
                        .param("per_page", "10"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(2))
                .andExpect(jsonPath("$.size").value(10));
    }
}