package com.ssafy.commitmood.domain.commit.mapper;

import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RankingMapper {

    /**
     * 전체 사용자 랭킹 조회 (커밋 수 기준)
     */
    List<UserRankingDto> findRankingByCommitCount(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 전체 사용자 랭킹 조회 (플래그 토큰 수 기준)
     */
    List<UserRankingDto> findRankingByFlaggedCount(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 전체 사용자 랭킹 조회 (욕설 수 기준)
     */
    List<UserRankingDto> findRankingBySwearCount(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 전체 사용자 랭킹 조회 (감정 점수 기준)
     */
    List<UserRankingDto> findRankingBySentimentScore(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 전체 사용자 랭킹 조회 (최근 활동 기준)
     */
    List<UserRankingDto> findRankingByRecentActivity(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 특정 사용자의 저장소별 통계 조회
     */
    List<UserRepoStatsDto> findUserRepoStats(
            @Param("userAccountId") Long userAccountId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 특정 사용자의 플래그 토큰 통계 조회
     */
    List<UserFlaggedStatsDto> findUserFlaggedStats(
            @Param("userAccountId") Long userAccountId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 특정 사용자의 감정 분석 통계 조회
     */
    UserSentimentStatsDto findUserSentimentStats(@Param("userAccountId") Long userAccountId);
}