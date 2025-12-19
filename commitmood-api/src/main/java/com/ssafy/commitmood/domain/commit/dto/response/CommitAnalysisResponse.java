package com.ssafy.commitmood.domain.commit.dto.response;

import com.ssafy.commitmood.domain.commit.entity.CommitAnalysis;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CommitAnalysisResponse (
        @Schema(description = "커밋 로그 ID", example = "1")
        Long commitLogId,

        @Schema(description = "플래그된 토큰 수", example = "3")
        Long flaggedCount,

        @Schema(description = "욕설 토큰 수", example = "1")
        Long swearCount,

        @Schema(description = "느낌표 토큰 수", example = "2")
        Long exclaimCount,

        @Schema(description = "이모지 토큰 수", example = "4")
        Long emojiCount,

        @Schema(description = "감정 분석 결과 (POSITIVE, NEGATIVE, NEUTRAL)", example = "POSITIVE")
        String sentiment,

        @Schema(description = "감정 점수", example = "0.85")
        BigDecimal sentimentScore,

        @Schema(description = "분석된 시각", example = "2025-12-01T12:00:00")
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
