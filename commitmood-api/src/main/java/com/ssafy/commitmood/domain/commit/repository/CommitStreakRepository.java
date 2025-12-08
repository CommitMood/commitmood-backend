package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.repository.mapper.CommitStreakMapper;
import com.ssafy.commitmood.domain.commit.repository.mapper.CommitStreakMapper.DailyCommitCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommitStreakRepository {

    private final CommitStreakMapper mapper;

    public List<DailyCommitCount> findDailyCommitCounts(Long userAccountId,
                                                          LocalDate startDate,
                                                          LocalDate endDate) {
        return mapper.findDailyCommitCounts(userAccountId, startDate, endDate);
    }
}
