package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import java.time.LocalDate;
import java.util.List;

public interface UserStreakRepository {

    /**
     * 특정 기간 동안 사용자의 일별 커밋 수 조회
     */
    List<DailyCommitCountDto> findDailyCommitCounts(
            Long userAccountId,
            LocalDate startDate,
            LocalDate endDate
    );

    /**
     * 특정 기간 동안 사용자의 총 커밋 일수 조회
     */
    Integer countTotalCommitDays(
            Long userAccountId,
            LocalDate startDate,
            LocalDate endDate
    );
}