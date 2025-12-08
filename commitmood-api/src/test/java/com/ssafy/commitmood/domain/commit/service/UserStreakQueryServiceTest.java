package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.StreakResponse;
import com.ssafy.commitmood.domain.commit.repository.CommitStreakRepository;
import com.ssafy.commitmood.domain.commit.repository.mapper.CommitStreakMapper.DailyCommitCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserStreakQueryServiceTest {

    @Mock
    private CommitStreakRepository repository;

    @InjectMocks
    private UserStreakQueryService service;

    @Test
    @DisplayName("주간 스트릭을 조회한다 (7일)")
    void getStreakByUserId_Week() {
        // given
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(6);

        DailyCommitCount count1 = new DailyCommitCount(today, 3);
        DailyCommitCount count2 = new DailyCommitCount(today.minusDays(6), 2);

        List<DailyCommitCount> mockCounts = List.of(count2, count1);

        given(repository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(mockCounts);

        StreakResponse response = service.getStreakByUserId(userAccountId, "week");

        assertThat(response.userAccountId()).isEqualTo(userAccountId);
        assertThat(response.commits()).hasSize(7);
        assertThat(response.start()).isEqualTo(startDate);
        assertThat(response.end()).isEqualTo(today);

        assertThat(response.commits().get(6)).isEqualTo(3); // today
        assertThat(response.commits().get(4)).isEqualTo(2); // today - 2
        assertThat(response.commits().get(5)).isEqualTo(0); // today - 1 (커밋 없음)
    }

    @Test
    @DisplayName("월간 스트릭을 조회한다 (28일)")
    void getStreakByUserId_Month() {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        given(repository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(List.of());

        StreakResponse response = service.getStreakByUserId(userAccountId, "month");

        assertThat(response.commits()).hasSize(28);
        assertThat(response.start()).isEqualTo(today.minusDays(27));
        assertThat(response.end()).isEqualTo(today);
        assertThat(response.commits()).allMatch(count -> count == 0);
    }

    @Test
    @DisplayName("연간 스트릭을 조회한다 (365일)")
    void getStreakByUserId_Year() {
        Long userAccountId = 1L;
        LocalDate today = LocalDate.now();

        given(repository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(List.of());

        StreakResponse response = service.getStreakByUserId(userAccountId, "year");

        assertThat(response.commits()).hasSize(365);
        assertThat(response.start()).isEqualTo(today.minusDays(364));
        assertThat(response.end()).isEqualTo(today);
    }

    @Test
    @DisplayName("옵션이 null이면 기본값(week) 적용")
    void getStreakByUserId_DefaultOption() {
        Long userAccountId = 1L;

        given(repository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(List.of());

        StreakResponse response = service.getStreakByUserId(userAccountId, null);

        assertThat(response.commits()).hasSize(7);
    }

    @Test
    @DisplayName("알 수 없는 옵션이면 기본값(week) 적용")
    void getStreakByUserId_UnknownOption() {
        Long userAccountId = 1L;

        given(repository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(List.of());

        StreakResponse response = service.getStreakByUserId(userAccountId, "unknown");

        assertThat(response.commits()).hasSize(7);
    }

    @Test
    @DisplayName("커밋이 없는 사용자의 스트릭 조회")
    void getStreakByUserId_NoCommits() {
        Long userAccountId = 1L;

        given(repository.findDailyCommitCounts(
                eq(userAccountId), any(LocalDate.class), any(LocalDate.class)))
                .willReturn(List.of());

        StreakResponse response = service.getStreakByUserId(userAccountId, "week");

        assertThat(response.commits()).hasSize(7);
        assertThat(response.commits()).allMatch(count -> count == 0);
    }
}
