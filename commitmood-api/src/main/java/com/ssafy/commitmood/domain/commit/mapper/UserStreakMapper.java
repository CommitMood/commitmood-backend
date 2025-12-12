package com.ssafy.commitmood.domain.commit.mapper;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserStreakMapper {

    /**
     * 특정 기간 동안 사용자의 일별 커밋 수 조회
     * @param userAccountId 사용자 계정 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 일별 커밋 통계 목록
     */
    List<DailyCommitCountDto> findDailyCommitCounts(
            @Param("userAccountId") Long userAccountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * 사용자의 총 커밋 일수 조회
     * @param userAccountId 사용자 계정 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 총 커밋 일수
     */
    Integer countTotalCommitDays(
            @Param("userAccountId") Long userAccountId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}