package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.dto.UserFlaggedStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.UserRepoStatsDto;
import com.ssafy.commitmood.domain.commit.dto.UserSentimentStatsDto;
import com.ssafy.commitmood.domain.commit.mapper.RankingMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RankingRepositoryImpl implements RankingRepository {

    private final RankingMapper rankingMapper;

    @Override
    public List<UserRankingDto> findRankingByCommitCount(int limit, int offset) {
        return rankingMapper.findRankingByCommitCount(limit, offset);
    }

    @Override
    public List<UserRankingDto> findRankingByFlaggedCount(int limit, int offset) {
        return rankingMapper.findRankingByFlaggedCount(limit, offset);
    }

    @Override
    public List<UserRankingDto> findRankingBySwearCount(int limit, int offset) {
        return rankingMapper.findRankingBySwearCount(limit, offset);
    }

    @Override
    public List<UserRankingDto> findRankingBySentimentScore(int limit, int offset) {
        return rankingMapper.findRankingBySentimentScore(limit, offset);
    }

    @Override
    public List<UserRankingDto> findRankingByRecentActivity(int limit, int offset) {
        return rankingMapper.findRankingByRecentActivity(limit, offset);
    }

    @Override
    public List<UserRepoStatsDto> findUserRepoStats(Long userAccountId, int limit, int offset) {
        return rankingMapper.findUserRepoStats(userAccountId, limit, offset);
    }

    @Override
    public List<UserFlaggedStatsDto> findUserFlaggedStats(Long userAccountId, int limit, int offset) {
        return rankingMapper.findUserFlaggedStats(userAccountId, limit, offset);
    }

    @Override
    public UserSentimentStatsDto findUserSentimentStats(Long userAccountId) {
        return rankingMapper.findUserSentimentStats(userAccountId);
    }
}