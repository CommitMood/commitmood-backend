package com.ssafy.commitmood.domain.commit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 사용자 랭킹 정보 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRankingDto {
    private Long userAccountId;
    private String githubLogin;
    private String githubAvatarUrl;
    private Long totalCommits;
    private Long totalFlagged;
    private Long totalSwear;
    private BigDecimal avgSentimentScore;
    private LocalDateTime lastCommitAt;
}