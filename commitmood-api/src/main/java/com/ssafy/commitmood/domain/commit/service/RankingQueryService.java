package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingListResponse;
import com.ssafy.commitmood.domain.commit.repository.RankingRepository;
import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper.RankingEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RankingQueryService {
    private final RankingRepository repository;

    public PageResponse<RankingListResponse> getRankings(String option, int page, int perPage) {
        int offset = (page - 1) * perPage;

        List<RankingEntry> entries = switch (option == null ? "commit_count" : option.toLowerCase()) {
            case "flagged_count" -> repository.findRankingByFlaggedCount(perPage, offset);
            case "swear_count" -> repository.findRankingBySwearCount(perPage, offset);
            case "sentiment_score" -> repository.findRankingBySentimentScore(perPage, offset);
            case "recent" -> repository.findRankingByRecent(
                    LocalDateTime.now().minusDays(30), perPage, offset);
            default -> repository.findRankingByCommitCount(perPage, offset);
        };

        int total = repository.countTotalUsers();

        List<RankingListResponse> content = entries.stream()
                .map(RankingListResponse::from)
                .toList();

        return PageResponse.of(content, page, perPage, total);
    }
}
