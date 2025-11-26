package com.ssafy.commitmood.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommitAnalysis {
    private Long id;
    private Long commitId;
    private Integer flaggedCount;
    private Integer swearCount;
    private Integer exclaimCount;
    private Integer emojiCount;
    private Sentiment sentiment;
    private BigDecimal sentimentScore;
    private LocalDateTime analyzedAt;

    public enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}