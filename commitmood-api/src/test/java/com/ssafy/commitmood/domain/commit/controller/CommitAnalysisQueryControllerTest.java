package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.common.exception.NotFoundException;
import com.ssafy.commitmood.domain.commit.dto.response.CommitAnalysisResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import com.ssafy.commitmood.domain.commit.service.CommitAnalysisQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CommitAnalysisQueryController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
        )
public class CommitAnalysisQueryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommitAnalysisQueryService service;

    private final String prefix = "";

    @Test
    @DisplayName("GET /{prefix}/commits/{commitLogId}/analysis - 커밋 분석 정보를 조회한다.")
    void getCommitAnalysis_Success() throws Exception {
        Long commitLogId = 1L;
        CommitAnalysisResponse commitAnalysis = CommitAnalysisResponse.builder()
                .id(1L)
                .commitLogId(commitLogId)
                .flaggedCount(2L)
                .swearCount(1L)
                .exclaimCount(3L)
                .emojiCount(4L)
                .sentiment(CommitAnalysis.Sentiment.POSITIVE)
                .sentimentScore(0.85)
                .analyzedAt(LocalDateTime.now())
                .build();

        given(service.getAnalysisByCommitLogId(commitLogId)).willReturn(commitAnalysis);

        var result = mockMvc.perform(get(prefix + "/commits/{commitLogId}/analysis", commitLogId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commitAnalysis.id()))
                .andExpect(jsonPath("$.commitLogId").value(commitAnalysis.commitLogId()))
                .andExpect(jsonPath("$.flaggedCount").value(commitAnalysis.flaggedCount()))
                .andExpect(jsonPath("$.swearCount").value(commitAnalysis.swearCount()))
                .andExpect(jsonPath("$.exclaimCount").value(commitAnalysis.exclaimCount()))
                .andExpect(jsonPath("$.emojiCount").value(commitAnalysis.emojiCount()))
                .andExpect(jsonPath("$.sentiment").value(commitAnalysis.sentiment().toString()))
                .andExpect(jsonPath("$.sentimentScore").value(commitAnalysis.sentimentScore()));
    }

    @Test
    @DisplayName("GET /{prefix}/commits/{commitLogId}/analysis - 커밋 분석 정보가 없을 때 404를 반환한다.")
    void getCommitAnalysis_NotFound() throws Exception {
        Long commitLogId = 999L;

        given(service.getAnalysisByCommitLogId(commitLogId))
                .willThrow(new NotFoundException("Commit analysis not found"));

        var result = mockMvc.perform(get(prefix + "/commits/{commitLogId}/analysis", commitLogId));

        result.andExpect(status().isNotFound());
    }
}
