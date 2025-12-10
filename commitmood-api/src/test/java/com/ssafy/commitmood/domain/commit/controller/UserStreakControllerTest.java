package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.UserStreakResponse;
import com.ssafy.commitmood.domain.commit.service.UserStreakService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserStreakController 테스트")
class UserStreakControllerTest {

    @Mock
    private UserStreakService userStreakService;

    @InjectMocks
    private UserStreakController userStreakController;

    @Test
    @DisplayName("사용자 스트릭 조회 성공 - month 옵션")
    void getUserStreakMonth() {
        UserStreakResponse mockResponse = new UserStreakResponse(
                5, 10, 20,
                Map.of(LocalDate.now(), 3L, LocalDate.now().minusDays(1), 2L)
        );
        when(userStreakService.getUserStreak(1L, "month")).thenReturn(mockResponse);

        ResponseEntity<UserStreakResponse> response = userStreakController.getUserStreak(1L, "month");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().currentStreak()).isEqualTo(5);
        assertThat(response.getBody().longestStreak()).isEqualTo(10);
        assertThat(response.getBody().totalCommitDays()).isEqualTo(20);
        verify(userStreakService).getUserStreak(1L, "month");
    }

    @Test
    @DisplayName("사용자 스트릭 조회 성공 - week 옵션")
    void getUserStreakWeek() {
        UserStreakResponse mockResponse = new UserStreakResponse(3, 5, 7, Map.of());
        when(userStreakService.getUserStreak(1L, "week")).thenReturn(mockResponse);

        ResponseEntity<UserStreakResponse> response = userStreakController.getUserStreak(1L, "week");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().currentStreak()).isEqualTo(3);
        verify(userStreakService).getUserStreak(1L, "week");
    }

    @Test
    @DisplayName("사용자 스트릭 조회 성공 - year 옵션")
    void getUserStreakYear() {
        UserStreakResponse mockResponse = new UserStreakResponse(15, 30, 100, Map.of());
        when(userStreakService.getUserStreak(1L, "year")).thenReturn(mockResponse);

        ResponseEntity<UserStreakResponse> response = userStreakController.getUserStreak(1L, "year");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().longestStreak()).isEqualTo(30);
    }

    @Test
    @DisplayName("사용자 스트릭 조회 실패 - 잘못된 옵션")
    void getUserStreakInvalidOption() {
        when(userStreakService.getUserStreak(anyLong(), anyString()))
                .thenThrow(new IllegalArgumentException("유효하지 않은 옵션입니다"));

        assertThatThrownBy(() -> userStreakController.getUserStreak(1L, "invalid"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 옵션입니다");
    }
}