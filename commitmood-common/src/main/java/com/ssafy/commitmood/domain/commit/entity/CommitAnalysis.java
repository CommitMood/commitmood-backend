package com.ssafy.commitmood.domain.commit.entity;

import static lombok.AccessLevel.PROTECTED;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "commitLogId", callSuper = false)
public class CommitAnalysis extends BaseTimeEntity {

    private Long id;
    private Long commitLogId;
    private Long flaggedCount;
    private Long swearCount;
    private Long exclaimCount;
    private Long emojiCount;
    private Sentiment sentiment;
    private BigDecimal sentimentScore;
    private LocalDateTime analyzedAt;

    private CommitAnalysis(
            Long commitLogId,
            Long flaggedCount,
            Long swearCount,
            Long exclaimCount,
            Long emojiCount,
            Sentiment sentiment,
            BigDecimal sentimentScore
    ) {
        this.commitLogId = commitLogId;
        this.flaggedCount = flaggedCount != null ? flaggedCount : 0L;
        this.swearCount = swearCount != null ? swearCount : 0L;
        this.exclaimCount = exclaimCount != null ? exclaimCount : 0L;
        this.emojiCount = emojiCount != null ? emojiCount : 0L;
        this.sentiment = sentiment;
        this.sentimentScore = sentimentScore;
        this.analyzedAt = LocalDateTime.now();
    }

    public static CommitAnalysis create(
            Long commitLogId,
            Long flaggedCount,
            Long swearCount,
            Long exclaimCount,
            Long emojiCount,
            Sentiment sentiment,
            BigDecimal sentimentScore
    ) {
        validate(commitLogId);
        validateSentimentScore(sentimentScore);
        return new CommitAnalysis(
                commitLogId,
                flaggedCount,
                swearCount,
                exclaimCount,
                emojiCount,
                sentiment,
                sentimentScore
        );
    }

    public void updateCounts(
            Long flaggedCount,
            Long swearCount,
            Long exclaimCount,
            Long emojiCount
    ) {
        this.flaggedCount = flaggedCount != null ? flaggedCount : this.flaggedCount;
        this.swearCount = swearCount != null ? swearCount : this.swearCount;
        this.exclaimCount = exclaimCount != null ? exclaimCount : this.exclaimCount;
        this.emojiCount = emojiCount != null ? emojiCount : this.emojiCount;
    }

    public void updateSentiment(Sentiment sentiment, BigDecimal sentimentScore) {
        validateSentimentScore(sentimentScore);
        this.sentiment = sentiment;
        this.sentimentScore = sentimentScore;
        this.analyzedAt = LocalDateTime.now();
    }

    private static void validate(Long commitLogId) {
        if (commitLogId == null) {
            throw new IllegalArgumentException("Commit Log ID는 필수입니다.");
        }
    }

    private static void validateSentimentScore(BigDecimal sentimentScore) {
        if (sentimentScore != null) {
            if (sentimentScore.compareTo(BigDecimal.valueOf(-1.00)) < 0 ||
                    sentimentScore.compareTo(BigDecimal.valueOf(1.00)) > 0) {
                throw new IllegalArgumentException("Sentiment Score는 -1.00에서 1.00 사이여야 합니다.");
            }
        }
    }

    public enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}