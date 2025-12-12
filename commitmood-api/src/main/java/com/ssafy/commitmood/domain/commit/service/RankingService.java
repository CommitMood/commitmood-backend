package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.common.RankingOptionsEnum;
import com.ssafy.commitmood.domain.commit.common.UserRankingDetailOptions;
import com.ssafy.commitmood.domain.commit.dto.UserRankingDto;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.dto.response.UserRankingDetailResponse;
import com.ssafy.commitmood.domain.commit.repository.RankingRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingService {

    private final RankingRepository rankingRepository;

    public RankingListResponse getRankingList(RankingOptionsEnum option, int page, int perPage) {
        int offset = (page - 1) * perPage;

        List<UserRankingDto> rankings = switch (option) {
            case commit_count -> rankingRepository.findRankingByCommitCount(perPage, offset);
            case flagged_count -> rankingRepository.findRankingByFlaggedCount(perPage, offset);
            case swear_count -> rankingRepository.findRankingBySwearCount(perPage, offset);
            case sentiment_score -> rankingRepository.findRankingBySentimentScore(perPage, offset);
            case recent -> rankingRepository.findRankingByRecentActivity(perPage, offset);
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
                var repoStats = rankingRepository.findUserRepoStats(userAccountId, perPage, offset);
                yield UserRankingDetailResponse.ofRepo(repoStats, page, perPage);
            }
            case flagged -> {
                var flaggedStats = rankingRepository.findUserFlaggedStats(userAccountId, perPage, offset);
                yield UserRankingDetailResponse.ofFlagged(flaggedStats, page, perPage);
            }
            case sentiment -> {
                var sentimentStats = rankingRepository.findUserSentimentStats(userAccountId);
                yield UserRankingDetailResponse.ofSentiment(sentimentStats, page, perPage);
            }
        };
    }
}
