package com.ssafy.commitmood.domain.commit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 플래그 토큰 통계 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFlaggedStatsDto {
    private String token;
    private String tokenType;
    private Long occurrenceCount;
    private Long totalWeight;
}