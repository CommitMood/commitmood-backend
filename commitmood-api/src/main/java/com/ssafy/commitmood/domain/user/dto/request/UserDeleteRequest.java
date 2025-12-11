package com.ssafy.commitmood.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 탈퇴 요청 DTO")
public record UserDeleteRequest(

        @Schema(
                description = "탈퇴 사유(선택 사항)",
                example = "더 이상 서비스를 사용하지 않음"
        )
        String reason
) {
}
