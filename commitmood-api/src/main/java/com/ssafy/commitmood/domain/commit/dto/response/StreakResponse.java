package com.ssafy.commitmood.domain.commit.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record StreakResponse(
        Long userAccountId,
        List<Integer> commits,
        LocalDate start,
        LocalDate end
) {
}
