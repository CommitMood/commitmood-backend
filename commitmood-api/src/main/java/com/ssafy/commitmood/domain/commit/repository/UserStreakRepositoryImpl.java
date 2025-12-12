package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import com.ssafy.commitmood.domain.commit.mapper.UserStreakMapper;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStreakRepositoryImpl implements UserStreakRepository {

    private final UserStreakMapper userStreakMapper;

    @Override
    public List<DailyCommitCountDto> findDailyCommitCounts(
            Long userAccountId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return userStreakMapper.findDailyCommitCounts(
                userAccountId,
                startDate,
                endDate
        );
    }

    @Override
    public Integer countTotalCommitDays(
            Long userAccountId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return userStreakMapper.countTotalCommitDays(
                userAccountId,
                startDate,
                endDate
        );
    }
}