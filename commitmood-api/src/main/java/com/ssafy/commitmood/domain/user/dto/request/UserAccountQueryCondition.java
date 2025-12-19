package com.ssafy.commitmood.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 조회 조건 DTO")
public record UserAccountQueryCondition(

        @Schema(description = "이메일로 검색", example = "gildong@example.com")
        String email,

        @Schema(description = "이름으로 검색", example = "홍길동")
        String name,

        @Schema(description = "GitHub 로그인명으로 검색", example = "hongildong")
        String githubLogin
) {
}
