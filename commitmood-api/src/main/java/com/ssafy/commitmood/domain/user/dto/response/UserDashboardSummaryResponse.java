package com.ssafy.commitmood.domain.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "대시보드 요약 정보 응답 DTO")
public record UserDashboardSummaryResponse(

        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "전체 커밋 수", example = "1523")
        Long totalCommits,

        @Schema(description = "전체 저장소 수", example = "12")
        Long totalRepos,

        @Schema(description = "전체 flagged token 발생 횟수", example = "87")
        Long flaggedTotal,

        @Schema(description = "욕설성 단어 발생 횟수", example = "5")
        Long swearTotal,

        @Schema(description = "이모지 사용 횟수", example = "42")
        Long emojiTotal,

        @Schema(description = "마지막 동기화 시각", example = "2025-12-11T14:20:30")
        LocalDateTime lastSyncedAt
) {
}
