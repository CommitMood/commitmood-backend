package com.ssafy.commitmood.domain.commit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.ssafy.commitmood.domain.commit.common.DateOptionsEnum;
import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import com.ssafy.commitmood.domain.commit.dto.response.UserStreakResponse;
import com.ssafy.commitmood.domain.commit.repository.UserStreakRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserStreakService 테스트")
public class UserStreakServiceTest {

    @Mock
    private UserStreakRepository userStreakRepository;

    @InjectMocks
    private UserStreakService userStreakService;

    @Test
    @DisplayName("주간 스트릭을 조회한다.")
    void getUserStreak_Week() {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        List<DailyCommitCountDto> dailyCommitCountDtoList = Arrays.asList(
                new DailyCommitCountDto(today, 3L),
                new DailyCommitCountDto(today.minusDays(1), 2L),
                new DailyCommitCountDto(today.minusDays(2), 0L)
        );

        given(userStreakRepository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)
        )).willReturn(dailyCommitCountDtoList);

        given(userStreakRepository.countTotalCommitDays(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)
        )).willReturn(3);

        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, DateOptionsEnum.week);

        assertThat(response).isNotNull();
        assertThat(response.currentStreak()).isEqualTo(3);
        assertThat(response.longestStreak()).isEqualTo(3);
        assertThat(response.totalCommitDays()).isEqualTo(3);
        assertThat(response.dailyCommits()).hasSize(3);
        assertThat(response.dailyCommits().get(today)).isEqualTo(3L);

        verify(userStreakRepository).findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class));
        verify(userStreakRepository).countTotalCommitDays(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("월간 스트릭을 조회한다.")
    void getUserStreak_Month() {
        Long userAccountId = 1L;

        given(userStreakRepository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)
        )).willReturn(Collections.emptyList());
        given(userStreakRepository.countTotalCommitDays(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)
        )).willReturn(0);

        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, DateOptionsEnum.month);

        assertThat(response).isNotNull();
        assertThat(response.currentStreak()).isEqualTo(0);
        assertThat(response.longestStreak()).isEqualTo(0);
        assertThat(response.totalCommitDays()).isEqualTo(0);

        verify(userStreakRepository).findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("연간 스트릭을 조회한다")
    void getUserStreak_Year() {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        // 3일 연속 스트릭
        List<DailyCommitCountDto> dailyCommits = Arrays.asList(
                new DailyCommitCountDto(today.minusDays(10), 1L),
                new DailyCommitCountDto(today.minusDays(11), 2L),
                new DailyCommitCountDto(today.minusDays(12), 1L)
        );

        given(userStreakRepository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(dailyCommits);
        given(userStreakRepository.countTotalCommitDays(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(5);

        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, DateOptionsEnum.year);

        assertThat(response).isNotNull();
        assertThat(response.longestStreak()).isEqualTo(3);
        assertThat(response.totalCommitDays()).isEqualTo(5);

        verify(userStreakRepository).findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    @DisplayName("현재 스트릭 계산 - 오늘 커밋이 없으면 어제부터 계산한다")
    void calculateCurrentStreak_NoTodayCommit() {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        List<DailyCommitCountDto> dailyCommits = Arrays.asList(
                new DailyCommitCountDto(today.minusDays(1), 2L),
                new DailyCommitCountDto(today.minusDays(2), 1L),
                new DailyCommitCountDto(today.minusDays(3), 3L)
        );

        given(userStreakRepository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(dailyCommits);
        given(userStreakRepository.countTotalCommitDays(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(3);

        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, DateOptionsEnum.week);

        assertThat(response.currentStreak()).isEqualTo(3);
    }

    @Test
    @DisplayName("최장 스트릭 계산 - 여러 연속 구간 중 최장을 찾는다")
    void calculateLongestStreak_MultipleStreaks() {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        // 2일, 5일, 3일 연속 스트릭 -> 5일이 정답임
        List<DailyCommitCountDto> dailyCommits = Arrays.asList(
                new DailyCommitCountDto(today.minusDays(1), 1L),
                new DailyCommitCountDto(today.minusDays(2), 1L),
                // 끊김
                new DailyCommitCountDto(today.minusDays(5), 1L),
                new DailyCommitCountDto(today.minusDays(6), 1L),
                new DailyCommitCountDto(today.minusDays(7), 1L),
                new DailyCommitCountDto(today.minusDays(8), 1L),
                new DailyCommitCountDto(today.minusDays(9), 1L),
                // 끊김
                new DailyCommitCountDto(today.minusDays(12), 1L),
                new DailyCommitCountDto(today.minusDays(13), 1L),
                new DailyCommitCountDto(today.minusDays(14), 1L)
        );

        given(userStreakRepository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(dailyCommits);
        given(userStreakRepository.countTotalCommitDays(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(10);

        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, DateOptionsEnum.month);

        assertThat(response.longestStreak()).isEqualTo(5); // 5일이 최장 스트릭
    }
}
