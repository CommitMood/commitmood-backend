package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommitAnalysisResponse(
        Long id,
        Long commitLogId,
        Long flaggedCount,
        Long swearCount,
        Long exclaimCount,
        Long emojiCount,
        CommitAnalysis.Sentiment sentiment,
        Double sentimentScore,
        LocalDateTime analyzedAt
) {
    // 정적 팩토리 메서드로 생성을 보다 명확히 정의함.
    public static CommitAnalysisResponse from(CommitAnalysis commitAnalysis) {
        return CommitAnalysisResponse.builder()
                .id(commitAnalysis.getId())
                .commitLogId(commitAnalysis.getCommitLogId())
                .flaggedCount(commitAnalysis.getFlaggedCount())
                .swearCount(commitAnalysis.getSwearCount())
                .exclaimCount(commitAnalysis.getExclaimCount())
                .emojiCount(commitAnalysis.getEmojiCount())
                .sentiment(commitAnalysis.getSentiment())
                .sentimentScore(commitAnalysis.getSentimentScore().doubleValue())
                .analyzedAt(commitAnalysis.getAnalyzedAt())
                .build();
    }
}
