package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CommitAnalysisResponse (
        Long commitLogId,
        Long flaggedCount,
        Long swearCount,
        Long exclaimCount,
        Long emojiCount,
        String sentiment,
        BigDecimal sentimentScore,
        LocalDateTime analyzedAt
)
{
    public static CommitAnalysisResponse from(CommitAnalysis analysis) {
        return new CommitAnalysisResponse(
                analysis.getCommitLogId(),
                analysis.getFlaggedCount(),
                analysis.getSwearCount(),
                analysis.getExclaimCount(),
                analysis.getEmojiCount(),
                analysis.getSentiment() != null ? analysis.getSentiment().name() : null,
                analysis.getSentimentScore(),
                analysis.getAnalyzedAt()
        );
    }
}
