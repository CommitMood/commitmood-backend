package com.ssafy.commitmood.domain.commit.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RankingMapper {
    /**
     * 커밋 수 기준 랭킹 조회
     */
    List<RankingEntry> findRankingByCommitCount(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 플래그 토큰 수 기준 랭킹 조회
     */
    List<RankingEntry> findRankingByFlaggedCount(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 욕설 수 기준 랭킹 조회
     */
    List<RankingEntry> findRankingBySwearCount(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 감정 점수 기준 랭킹 조회
     */
    List<RankingEntry> findRankingBySentimentScore(
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 최근 활동 기준 랭킹 조회 (최근 30일)
     */
    List<RankingEntry> findRankingByRecent(
            @Param("startDate") LocalDateTime startDate,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    /**
     * 전체 사용자 수 조회
     */
    int countTotalUsers();

    record RankingEntry(
            Long userAccountId,
            String githubLogin,
            String githubAvatarUrl,
            Integer commitCount,
            Integer flaggedCount,
            Integer swearCount,
            Double sentimentScore,
            Integer recentCommitCount
    ) {
    }
}
