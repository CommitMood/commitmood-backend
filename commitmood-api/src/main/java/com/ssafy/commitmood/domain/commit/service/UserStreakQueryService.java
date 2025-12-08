package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.StreakResponse;
import com.ssafy.commitmood.domain.commit.repository.CommitStreakRepository;
import com.ssafy.commitmood.domain.commit.repository.mapper.CommitStreakMapper.DailyCommitCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserStreakQueryService {

    private final CommitStreakRepository repository;

    public StreakResponse getStreakByUserId(Long userAccountId, String option) {
        StreakPeriod period = calculatePeriod(option);

        List<DailyCommitCount> dailyCounts = repository.findDailyCommitCounts(userAccountId, period.startDate, period.endDate);

        // 날짜별 커밋 수를 Map으로 변환해서 반환
        Map<LocalDate, Integer> commitCountMap = dailyCounts.stream()
                .collect(Collectors.toMap(DailyCommitCount::commitDate, DailyCommitCount::commitCount));

        List<Integer> commits = period.startDate.datesUntil(period.endDate.plusDays(1))
                .map(date -> commitCountMap.getOrDefault(date, 0))
                .toList();

        return StreakResponse.builder()
                .userAccountId(userAccountId)
                .commits(commits)
                .start(period.startDate)
                .end(period.endDate)
                .build();
    }

    /**
     * 현재 날짜를 end로 해서 날짜 계산
     * @param option null 혹은 적합한 값
     * @return start - end 기간
     */
    private StreakPeriod calculatePeriod(String option) {
        LocalDate now = LocalDate.now();
        LocalDate startDate;

        switch (option == null ? "week" : option.toLowerCase()) {
            case "month" -> startDate = now.minusDays(29); // 30일
            case "year" -> startDate = now.minusDays(364); // 365일
            case "week" -> startDate = now.minusDays(6); // 7일
            default -> startDate = now.minusDays(6);
        }

        return new StreakPeriod(startDate, now);
    }

    private record StreakPeriod(LocalDate startDate, LocalDate endDate) {}
}
