package com.ssafy.commitmood.domain.commit.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CommitStreakMapper {

    /**
     * 특정 기간 동안 일별 커밋 수 조회
     * @param userAccountId 사용자 ID
     * @param startDate 시작 날짜 (포함)
     * @param endDate 종료 날짜 (포함)
     * @return 날짜별 커밋 수 리스트
     */
    List<DailyCommitCount> findDailyCommitCounts(
            @Param("userAccountId") Long userAccountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 일별 커밋 수 기록
     * @param commitDate 날짜
     * @param commitCount 커밋 수
     */
    record DailyCommitCount(LocalDate commitDate, int commitCount) {
    }
}
