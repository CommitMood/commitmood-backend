package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record UserStreakResponse (
        Integer currentStreak,
        Integer longestStreak,
        Integer totalCommitDays,
        Map<LocalDate, Long> dailyCommits
) {
    public static UserStreakResponse of(
            Integer currentStreak,
            Integer longestStreak,
            Integer totalCommitDays,
            List<DailyCommitCountDto> dailyCommitCounts
    ) {
        Map<LocalDate, Long> dailyCommitsMap = dailyCommitCounts.stream()
                .collect(Collectors.toMap(
                        DailyCommitCountDto::getCommitDate,
                        DailyCommitCountDto::getCommitCount
                ));

        return new UserStreakResponse(
                currentStreak,
                longestStreak,
                totalCommitDays,
                dailyCommitsMap
        );
    }
}