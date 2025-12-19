package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Schema(description = "사용자 스트릭 응답")
public record UserStreakResponse (
        @Schema(description = "현재 연속 커밋 일수", example = "7")
        Integer currentStreak,

        @Schema(description = "최장 연속 커밋 일수", example = "21")
        Integer longestStreak,

        @Schema(description = "총 커밋 일수", example = "150")
        Integer totalCommitDays,

        @Schema(description = "일별 커밋 수 (날짜: 커밋 수)")
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