package com.ssafy.commitmood.domain.auth.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id")
public class AccessToken extends BaseTimeEntity {

    private Long id;
    private Long userAccountId;
    private String accessToken;
    private String tokenType;
    private String scope;
    private LocalDateTime expiresAt;

    private AccessToken(
            Long userAccountId,
            String accessToken,
            String tokenType,
            String scope,
            LocalDateTime expiresAt
    ) {
        this.userAccountId = userAccountId;
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.expiresAt = expiresAt;
    }

    public static AccessToken create(
            Long userAccountId,
            String accessToken,
            String tokenType,
            String scope,
            LocalDateTime expiresAt
    ) {
        validate(userAccountId, accessToken);
        return new AccessToken(
                userAccountId,
                accessToken,
                tokenType != null ? tokenType : "BEARER",
                scope,
                expiresAt
        );
    }

    public void updateToken(
            String accessToken,
            String scope,
            LocalDateTime expiresAt
    ) {
        if (accessToken != null && !accessToken.isBlank()) {
            this.accessToken = accessToken;
        }
        this.scope = scope;
        this.expiresAt = expiresAt;
    }

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    private static void validate(Long userAccountId, String accessToken) {
        if (userAccountId == null) {
            throw new IllegalArgumentException("User Account ID는 필수입니다.");
        }
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("Access Token은 비어 있을 수 없습니다.");
        }
    }
}