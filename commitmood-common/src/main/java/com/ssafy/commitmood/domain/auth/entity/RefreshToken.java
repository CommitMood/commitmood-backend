package com.ssafy.commitmood.domain.auth.entity;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class RefreshToken extends BaseTimeEntity {

    private Long id;
    private Long userAccountId;
    private String token;
    private LocalDateTime expiresAt;

    private RefreshToken(Long userAccountId, String token, LocalDateTime expiresAt) {
        this.userAccountId = userAccountId;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public static RefreshToken create(Long userAccountId, String token, LocalDateTime expiresAt) {
        if (userAccountId == null) {
            throw new IllegalArgumentException("User Account ID는 필수입니다.");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token은 비어 있을 수 없습니다.");
        }
        return new RefreshToken(userAccountId, token, expiresAt);
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }
}
