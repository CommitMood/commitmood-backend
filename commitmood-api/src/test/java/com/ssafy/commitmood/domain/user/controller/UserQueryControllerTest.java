package com.ssafy.commitmood.domain.user.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.service.UserQueryService;
import java.time.LocalDateTime;
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
class UserQueryControllerTest {

    @Mock
    private UserQueryService userQueryService;

    @InjectMocks
    private UserQueryController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    // 1) email 기반 조회
    @Test
    @DisplayName("GET /users?email=abc 이메일 Prefix 조회 성공")
    void getUserByEmail_success() throws Exception {
        String email = "abc";
        UserAccountResponse mockRes = mockResponse();

        given(userQueryService.getUserByEmail(email)).willReturn(mockRes);

        mockMvc.perform(get("/users").param("email", email))
                .andExpect(status().isOk());

        then(userQueryService).should().getUserByEmail(eq(email));
    }

    // 2) githubLogin 기반 조회
    @Test
    @DisplayName("GET /users?githubLogin=dev 로그인 Prefix 조회 성공")
    void getUserByGithubLogin_success() throws Exception {
        String login = "dev";
        UserAccountResponse mockRes = mockResponse();

        given(userQueryService.getUserByGithubLogin(login)).willReturn(mockRes);

        mockMvc.perform(get("/users").param("githubLogin", login))
                .andExpect(status().isOk());

        then(userQueryService).should().getUserByGithubLogin(eq(login));
    }

    // 3) name 기반 조회 (default 분기 작동 여부가 핵심)
    @Test
    @DisplayName("GET /users?name=han Name Prefix 조회 성공 (Default branch)")
    void getUserByName_success() throws Exception {
        String name = "han";
        UserAccountResponse mockRes = mockResponse();

        given(userQueryService.getUserByName(name)).willReturn(mockRes);

        mockMvc.perform(get("/users").param("name", name))
                .andExpect(status().isOk());

        then(userQueryService).should().getUserByName(eq(name));
    }

    // ================= Utility ==================

    private UserAccountResponse mockResponse() {
        return new UserAccountResponse(
                1L, 100L,
                "login", "email",
                "avatar", "name",
                LocalDateTime.now(), LocalDateTime.now()
        );
    }
}