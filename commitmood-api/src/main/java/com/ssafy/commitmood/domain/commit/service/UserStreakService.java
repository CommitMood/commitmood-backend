package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import com.ssafy.commitmood.domain.commit.dto.response.UserStreakResponse;
import com.ssafy.commitmood.domain.commit.repository.UserStreakMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStreakService {

    private final UserStreakMapper userStreakMapper;

    public UserStreakResponse getUserStreak(Long userAccountId, String option) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = calculateStartDate(endDate, option);

        List<DailyCommitCountDto> dailyCommits = userStreakMapper.findDailyCommitCounts(
                userAccountId, startDate, endDate
        );

        Integer totalCommitDays = userStreakMapper.countTotalCommitDays(
                userAccountId, startDate, endDate
        );

        Integer currentStreak = calculateCurrentStreak(dailyCommits, endDate);

        Integer longestStreak = calculateLongestStreak(dailyCommits);

        return UserStreakResponse.of(
                currentStreak,
                longestStreak,
                totalCommitDays != null ? totalCommitDays : 0,
                dailyCommits
        );
    }

    /**
     * 옵션에 따른 시작 날짜 계산
     */
    private LocalDate calculateStartDate(LocalDate endDate, String option) {
        return switch (option.toLowerCase()) {
            case "week" -> endDate.minusWeeks(1);
            case "month" -> endDate.minusMonths(1);
            case "year" -> endDate.minusYears(1);
            default -> throw new IllegalArgumentException("유효하지 않은 옵션입니다: " + option);
        };
    }

    /**
     * 현재 연속 커밋 일수 계산
     * 오늘 또는 어제부터 시작하여 연속된 커밋 일수를 계산
     */
    private Integer calculateCurrentStreak(List<DailyCommitCountDto> dailyCommits, LocalDate endDate) {
        if (dailyCommits.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate checkDate = endDate;

        // 오늘 커밋이 없으면 어제부터 시작
        boolean hasTodayCommit = dailyCommits.stream()
                .anyMatch(dc -> dc.getCommitDate().equals(endDate));

        if (!hasTodayCommit) {
            checkDate = endDate.minusDays(1);
        }

        // 연속된 날짜 확인
        for (int i = 0; i <= 365; i++) { // 최대 1년
            LocalDate targetDate = checkDate.minusDays(i);
            boolean hasCommit = dailyCommits.stream()
                    .anyMatch(dc -> dc.getCommitDate().equals(targetDate));

            if (hasCommit) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    /**
     * 최장 연속 커밋 일수 계산
     */
    private Integer calculateLongestStreak(List<DailyCommitCountDto> dailyCommits) {
        if (dailyCommits.isEmpty()) {
            return 0;
        }

        int maxStreak = 0;
        int currentStreak = 1;

        List<LocalDate> sortedDates = dailyCommits.stream()
                .map(DailyCommitCountDto::getCommitDate)
                .sorted()
                .toList();

        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate prevDate = sortedDates.get(i - 1);
            LocalDate currDate = sortedDates.get(i);

            // 연속된 날짜인지 확인 (하루 차이)
            if (prevDate.plusDays(1).equals(currDate)) {
                currentStreak++;
            } else {
                maxStreak = Math.max(maxStreak, currentStreak);
                currentStreak = 1;
            }
        }

        return Math.max(maxStreak, currentStreak);
    }
}
