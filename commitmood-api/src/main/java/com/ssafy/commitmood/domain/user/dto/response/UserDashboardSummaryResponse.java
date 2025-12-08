package com.ssafy.commitmood.domain.user.dto.response;

import java.time.LocalDateTime;

public record UserDashboardSummaryResponse(
        Long userId,
        Long totalCommits,
        Long totalRepos,
        Long flaggedTotal,
        Long swearTotal,
        Long emojiTotal,
        LocalDateTime lastSyncedAt
) {
}
