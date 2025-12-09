package com.ssafy.commitmood.domain.commit.repository.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRankingMapper {
    List<UserRankingDetail> findUserRankingByRepo (
        @Param("userAccountId") Long userAccountId,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    List<UserRankingDetail> findUserRankingByFlagged(
            @Param("userAccountId") Long userAccountId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    List<UserRankingDetail> findUserRankingBySentiment(
            @Param("userAccountId") Long userAccountId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    int countUserRankingByRepo(@Param("userAccountId") Long userAccountId);

    int countUserRankingByFlagged(@Param("userAccountId") Long userAccountId);

    int countUserRankingBySentiment(@Param("userAccountId") Long userAccountId);

    record UserRankingDetail(
            Long commitLogId,
            String repoFullName,
            String message,
            String committedAt,
            Long flaggedCount,
            Long swearCount,
            Long sentiment,
            Double sentimentScore,
            String githubUrl
    ) {}
}
