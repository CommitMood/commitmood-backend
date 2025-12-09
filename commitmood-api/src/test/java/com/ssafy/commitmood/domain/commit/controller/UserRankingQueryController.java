package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.service.UserRankingQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserRankingQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserRankingQueryController {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRankingQueryService service;

    private final String prefix = "";

    @Test
    @DisplayName("GET /{prefix}/users/{userAccountId}/ranking - 저장소별 랭킹 조회")
    void getUserRanking_Repo() throws Exception {

    }
}
