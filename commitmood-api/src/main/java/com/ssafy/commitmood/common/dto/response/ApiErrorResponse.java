package com.ssafy.commitmood.common.dto.response;

public record ApiErrorResponse(
        int status,
        String message
) {
    public static ApiErrorResponse of(int status, String message) {
        return new ApiErrorResponse(status, message);
    }
}