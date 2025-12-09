package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.dto.response.RankingDetailResponse;
import com.ssafy.commitmood.domain.commit.repository.UserRankingRepository;
import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper.UserRankingDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRankingQueryService {

    private final UserRankingRepository repository;

    public PageResponse<RankingDetailResponse> getUserRanking(
            Long userAccountId,
            String detail,
            int page,
            int perPage
    ) {
        int offset = (page - 1) * perPage;

        List<UserRankingDetail> details;
        int total;

        switch (detail == null ? "repo" : detail.toLowerCase()) {
            case "flagged" -> {
                details = repository.findUserRankingByFlagged(userAccountId, perPage, offset);
                total = repository.countUserRankingByFlagged(userAccountId);
            }
            case "sentiment" -> {
                details = repository.findUserRankingBySentiment(userAccountId, perPage, offset);
                total = repository.countUserRankingBySentiment(userAccountId);
            }
            default -> {
                details = repository.findUserRankingByRepo(userAccountId, perPage, offset);
                total = repository.countUserRankingByRepo(userAccountId);
            }
        }

        List<RankingDetailResponse> content = details.stream()
                .map(RankingDetailResponse::from)
                .toList();

        return PageResponse.of(content, page, perPage, total);
    }
}
