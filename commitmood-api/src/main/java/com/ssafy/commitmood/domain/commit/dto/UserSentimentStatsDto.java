package com.ssafy.commitmood.domain.commit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 사용자 감정 분석 통계 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSentimentStatsDto {
    private Long positiveCount;
    private Long neutralCount;
    private Long negativeCount;
    private BigDecimal avgSentimentScore;
    private BigDecimal maxSentimentScore;
    private BigDecimal minSentimentScore;
}