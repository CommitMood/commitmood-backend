package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
import java.util.List;

public interface RankingRepository {

    /**
     * 전체 사용자 랭킹 조회 (커밋 수 기준)
     */
    List<UserRankingDto> findRankingByCommitCount(int limit, int offset);

    /**
     * 전체 사용자 랭킹 조회 (플래그 토큰 수 기준)
     */
    List<UserRankingDto> findRankingByFlaggedCount(int limit, int offset);

    /**
     * 전체 사용자 랭킹 조회 (욕설 수 기준)
     */
    List<UserRankingDto> findRankingBySwearCount(int limit, int offset);

    /**
     * 전체 사용자 랭킹 조회 (감정 점수 기준)
     */
    List<UserRankingDto> findRankingBySentimentScore(int limit, int offset);

    /**
     * 전체 사용자 랭킹 조회 (최근 활동 기준)
     */
    List<UserRankingDto> findRankingByRecentActivity(int limit, int offset);

    /**
     * 특정 사용자의 저장소별 통계 조회
     */
    List<UserRepoStatsDto> findUserRepoStats(Long userAccountId, int limit, int offset);

    /**
     * 특정 사용자의 플래그 토큰 통계 조회
     */
    List<UserFlaggedStatsDto> findUserFlaggedStats(Long userAccountId, int limit, int offset);

    /**
     * 특정 사용자의 감정 분석 통계 조회
     */
    UserSentimentStatsDto findUserSentimentStats(Long userAccountId);
}