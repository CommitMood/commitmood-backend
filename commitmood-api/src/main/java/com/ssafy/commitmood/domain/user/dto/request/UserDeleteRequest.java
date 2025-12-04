package com.ssafy.commitmood.domain.user.dto.request;

public record UserDeleteRequest(
        String reason  // 선택 사항
) {
}
