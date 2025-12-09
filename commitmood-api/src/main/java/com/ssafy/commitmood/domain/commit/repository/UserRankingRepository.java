package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper;
import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper.UserRankingDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRankingRepository {

    private final UserRankingMapper mapper;

    public List<UserRankingDetail> findUserRankingByRepo(Long userAccountId, int limit, int offset) {
        return mapper.findUserRankingByRepo(userAccountId, limit, offset);
    }

    public List<UserRankingDetail> findUserRankingByFlagged(Long userAccountId, int limit, int offset) {
        return mapper.findUserRankingByFlagged(userAccountId, limit, offset);
    }

    public List<UserRankingDetail> findUserRankingBySentiment(Long userAccountId, int limit, int offset) {
        return mapper.findUserRankingBySentiment(userAccountId, limit, offset);
    }

    public int countUserRankingByRepo(Long userAccountId) {
        return mapper.countUserRankingByRepo(userAccountId);
    }

    public int countUserRankingByFlagged(Long userAccountId) {
        return mapper.countUserRankingByFlagged(userAccountId);
    }

    public int countUserRankingBySentiment(Long userAccountId) {
        return mapper.countUserRankingBySentiment(userAccountId);
    }
}
