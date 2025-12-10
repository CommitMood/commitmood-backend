package com.ssafy.commitmood.domain.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.service.UserCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserCommandControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserCommandService userCommandService;

    @InjectMocks
    private UserCommandController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("프로필 업데이트 API는 정상 응답을 반환해야 한다")
    void updateProfile_success() throws Exception {
        // given
        Long userId = 1L;
        GithubProfileUpdateRequest request = new GithubProfileUpdateRequest(
                "test@mail.com",
                "avatar.png",
                "New Name"
        );

        // when & then
        mockMvc.perform(patch("/users/{id}/github/profile", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        then(userCommandService).should()
                .updateUserProfileFromGithub(eq(userId), any(GithubProfileUpdateRequest.class));
    }

    @Test
    @DisplayName("사용자 삭제 API는 200 또는 204 성공 코드를 반환해야 한다")
    void deleteUser_success() throws Exception {
        // given
        Long userId = 10L;

        // when & then
        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk()); // 컨트롤러가 void라면 기본 200

        then(userCommandService).should().deleteUser(eq(userId));
    }
}