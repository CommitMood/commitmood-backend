package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper;
import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper.RankingEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RankingRepository {

    private final RankingMapper mapper;

    public List<RankingEntry> findRankingByCommitCount(int limit, int offset) {
        return mapper.findRankingByCommitCount(limit, offset);
    }

    public List<RankingEntry> findRankingByFlaggedCount(int limit, int offset) {
        return mapper.findRankingByFlaggedCount(limit, offset);
    }

    public List<RankingEntry> findRankingBySwearCount(int limit, int offset) {
        return mapper.findRankingBySwearCount(limit, offset);
    }

    public List<RankingEntry> findRankingBySentimentScore(int limit, int offset) {
        return mapper.findRankingBySentimentScore(limit, offset);
    }

    public List<RankingEntry> findRankingByRecent(java.time.LocalDateTime startDate, int limit, int offset) {
        return mapper.findRankingByRecent(startDate, limit, offset);
    }

    public int countTotalUsers() {
        return mapper.countTotalUsers();
    }
}
