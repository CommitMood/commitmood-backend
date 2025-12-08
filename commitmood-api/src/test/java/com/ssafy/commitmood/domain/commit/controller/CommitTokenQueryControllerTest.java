package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenResponse;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken.TokenType;
import com.ssafy.commitmood.domain.commit.service.CommitTokenQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = CommitTokenQueryController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
public class CommitTokenQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommitTokenQueryService service;

    private final String prefix = "";

    @Test
    @DisplayName("Get /{prefix}/commits/{commitLogId}/tokens - 플래그 토큰 목록을 조회한다.")
    void getCommitTokens_Success() throws Exception {
        Long commitLogId = 1L;

        List<FlaggedTokenResponse> tokenResponses = List.of(
                FlaggedTokenResponse.builder()
                        .flaggedTokenId(1L)
                        .commitLogId(commitLogId)
                        .token("damn")
                        .tokenType(TokenType.SWEAR)
                        .weight(10L)
                        .createdAt(LocalDateTime.now())
                        .build(),
                FlaggedTokenResponse.builder()
                        .flaggedTokenId(2L)
                        .commitLogId(commitLogId)
                        .token("😀")
                        .tokenType(TokenType.EMOJI)
                        .weight(5L)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        FlaggedTokenListResponse response = FlaggedTokenListResponse.builder()
                .commitLogId(commitLogId)
                .tokens(tokenResponses)
                .totalCount(2)
                .build();

        given(service.getTokensByCommitLogId(commitLogId))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/commits/{commitLogId}/tokens", commitLogId));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.commitLogId").value(commitLogId))
            .andExpect(jsonPath("$.totalCount").value(2))
            .andExpect(jsonPath("$.tokens").isArray())
            .andExpect(jsonPath("$.tokens[0].token").value("damn"))
            .andExpect(jsonPath("$.tokens[0].tokenType").value("SWEAR"))
            .andExpect(jsonPath("$.tokens[0].weight").value(10))
            .andExpect(jsonPath("$.tokens[1].token").value("😀"))
            .andExpect(jsonPath("$.tokens[1].tokenType").value("EMOJI"))
            .andExpect(jsonPath("$.tokens[1].weight").value(5));
    }

    @Test
    @DisplayName("Get /{prefix}/commits/{commitLogId}/tokens - 토큰이 없는 커밋의 경우 빈 리스트를 반환한다.")
    void getCommitTokens_EmptyList() throws Exception {
        Long commitLogId = 1L;
        FlaggedTokenListResponse response = FlaggedTokenListResponse.builder()
                .commitLogId(commitLogId)
                .tokens(List.of())
                .totalCount(0)
                .build();

        given(service.getTokensByCommitLogId(commitLogId))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/commits/{commitLogId}/tokens", commitLogId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.commitLogId").value(commitLogId))
                .andExpect(jsonPath("$.tokens").isEmpty())
                .andExpect(jsonPath("$.totalCount").value(0));
    }
}
