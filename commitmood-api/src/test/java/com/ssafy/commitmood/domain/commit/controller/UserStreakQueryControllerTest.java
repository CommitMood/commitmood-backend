package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.StreakResponse;
import com.ssafy.commitmood.domain.commit.service.UserStreakQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserStreakQueryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserStreakQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserStreakQueryService service;

    private final String prefix = "";

    @Test
    @DisplayName("GET /{prefix}/users/{userAccountId}/streak - 주간 스트릭 조회")
    void getUserStreak_Week() throws Exception {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        StreakResponse response = StreakResponse.builder()
                .userAccountId(userAccountId)
                .commits(List.of(0, 1, 2, 0, 3, 1, 4))
                .start(startDate)
                .end(today)
                .build();

        given(service.getStreakByUserId(userAccountId, "week"))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/users/{userAccountId}/streak", userAccountId)
                        .param("option", "week"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.userAccountId").value(userAccountId))
                .andExpect(jsonPath("$.commits").isArray())
                .andExpect(jsonPath("$.commits.length()").value(7))
                .andExpect(jsonPath("$.start").value(startDate.toString()))
                .andExpect(jsonPath("$.end").value(today.toString()));
    }

    @Test
    @DisplayName("GET /{prefix}/users/{userAccountId}/streak - 월간 스트릭 조회")
    void getUserStreak_Month() throws Exception {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(27);

        List<Integer> monthlyCommits = today.minusDays(27).datesUntil(today.plusDays(1))
                .map(date -> 1)
                .toList();

        StreakResponse response = StreakResponse.builder()
                .userAccountId(userAccountId)
                .commits(monthlyCommits)
                .start(startDate)
                .end(today)
                .build();

        given(service.getStreakByUserId(userAccountId, "month"))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/users/{userAccountId}/streak", userAccountId)
                        .param("option", "month"));
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.commits.length()").value(28));
    }

    @Test
    @DisplayName("GET /{prefix}/users/{userAccountId}/streak - 연간 스트릭 조회")
    void getUserStreak_Year() throws Exception {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(364);

        List<Integer> yearlyCommits = today.minusDays(364).datesUntil(today.plusDays(1))
                .map(date -> 0)
                .toList();

        StreakResponse response = StreakResponse.builder()
                .userAccountId(userAccountId)
                .commits(yearlyCommits)
                .start(startDate)
                .end(today)
                .build();

        given(service.getStreakByUserId(userAccountId, "year"))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/users/{userAccountId}/streak", userAccountId)
                        .param("option", "year"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.commits.length()").value(365));
    }

    @Test
    @DisplayName("GET /{prefix}/users/{userAccountId}/streak - 옵션 없이 조회 (기본값 week)")
    void getUserStreak_DefaultOption() throws Exception {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        StreakResponse response = StreakResponse.builder()
                .userAccountId(userAccountId)
                .commits(List.of(0, 0, 0, 0, 0, 0, 0))
                .start(today.minusDays(6))
                .end(today)
                .build();

        given(service.getStreakByUserId(userAccountId, "week"))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/users/{userAccountId}/streak", userAccountId));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.commits.length()").value(7));
    }

    @Test
    @DisplayName("GET /{prefix}/users/{userAccountId}/streak - 커밋이 없는 경우")
    void getUserStreak_NoCommits() throws Exception {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        StreakResponse response = StreakResponse.builder()
                .userAccountId(userAccountId)
                .commits(List.of(0, 0, 0, 0, 0, 0, 0))
                .start(today.minusDays(6))
                .end(today)
                .build();

        given(service.getStreakByUserId(userAccountId, "week"))
                .willReturn(response);

        var result = mockMvc.perform(get(prefix + "/users/{userAccountId}/streak", userAccountId)
                        .param("option", "week"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.commits").isArray())
                .andExpect(jsonPath("$.commits[0]").value(0))
                .andExpect(jsonPath("$.commits[6]").value(0));
    }
}
