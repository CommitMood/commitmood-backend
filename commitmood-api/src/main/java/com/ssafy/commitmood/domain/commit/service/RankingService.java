package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.common.RankingOptionsEnum;
import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.UserRankingDetailResponse;
import com.ssafy.commitmood.domain.commit.repository.RankingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {
    private final RankingMapper rankingMapper;

    public RankingListResponse getRankingList(RankingOptionsEnum option, int page, int perPage) {
        int offset = (page - 1) * perPage;

        List<UserRankingDto> rankings = switch (option) {
            case commit_count -> rankingMapper.findRankingByCommitCount(perPage, offset);
            case flagged_count -> rankingMapper.findRankingByFlaggedCount(perPage, offset);
            case swear_count -> rankingMapper.findRankingBySwearCount(perPage, offset);
            case sentiment_score -> rankingMapper.findRankingBySentimentScore(perPage, offset);
            case recent -> rankingMapper.findRankingByRecentActivity(perPage, offset);
        };

        return RankingListResponse.of(rankings, page, perPage);
    }

    public UserRankingDetailResponse getUserRankingDetail(
            Long userAccountId, UserRankingDetailOptions detail, int page, int perPage
    ) {
        int offset = (page - 1) * perPage;

        if (detail == null) {
            throw new IllegalArgumentException("유효하지 않은 상세 타입입니다: " + detail);
        }

        return switch (detail) {
            case repo -> {
                var repoStats = rankingMapper.findUserRepoStats(userAccountId, perPage, offset);
                yield UserRankingDetailResponse.ofRepo(repoStats, page, perPage);
            }
            case flagged -> {
                var flaggedStats = rankingMapper.findUserFlaggedStats(userAccountId, perPage, offset);
                yield UserRankingDetailResponse.ofFlagged(flaggedStats, page, perPage);
            }
            case sentiment -> {
                var sentimentStats = rankingMapper.findUserSentimentStats(userAccountId);
                yield UserRankingDetailResponse.ofSentiment(sentimentStats, page, perPage);
            }
        };
    }
}
