package com.ssafy.commitmood.domain.user.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.service.UserQueryService;
import com.ssafy.commitmood.domain.user.service.UserSearchService;
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

    @Mock
    private UserSearchService userSearchService;

    @InjectMocks
    private UserQueryController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/users/{id} 사용자 단건 조회 성공")
    void getUserById_success() throws Exception {
        // given
        Long id = 1L;
        LocalDateTime now = LocalDateTime.now();

        UserAccountResponse mockResponse = new UserAccountResponse(
                id,
                100L,
                "devys",
                "mail",
                "avatar",
                "name",
                now,
                now
        );

        given(userQueryService.getUserById(id)).willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk());

        then(userQueryService).should().getUserById(eq(id));
    }

    @Test
    @DisplayName("GET /api/users/login/{githubLogin} GitHub 로그인 ID로 조회 성공")
    void getUserByLogin_success() throws Exception {
        // given
        String login = "devys";
        LocalDateTime now = LocalDateTime.now();

        UserAccountResponse mockResponse = new UserAccountResponse(
                1L,
                100L,
                login,
                "mail",
                "avatar",
                "name",
                now,
                now
        );

        given(userQueryService.getUserByGithubLogin(login)).willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/users/login/{githubLogin}", login))
                .andExpect(status().isOk());

        then(userQueryService).should().getUserByGithubLogin(eq(login));
    }
}