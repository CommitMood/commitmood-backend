package com.ssafy.commitmood.domain.user.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;

import com.ssafy.commitmood.domain.common.entity.BaseTimeEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "githubUserId")
public class UserAccount extends BaseTimeEntity {

    private Long id;
    private Long githubUserId;
    private String githubLogin;
    private String githubEmail;
    private String githubAvatarUrl;
    private String githubName;
    private LocalDateTime lastSyncedAt;

    private UserAccount(
            Long githubUserId,
            String githubLogin,
            String githubEmail,
            String githubAvatarUrl,
            String githubName
    ) {
        this.githubUserId = githubUserId;
        this.githubLogin = githubLogin;
        this.githubEmail = githubEmail;
        this.githubAvatarUrl = githubAvatarUrl;
        this.githubName = githubName;
    }

    public static UserAccount create(
            Long githubUserId,
            String githubLogin,
            String githubEmail,
            String githubAvatarUrl,
            String githubName
    ) {
        validate(githubUserId, githubLogin);
        return new UserAccount(
                githubUserId,
                githubLogin,
                githubEmail,
                githubAvatarUrl,
                githubName
        );
    }

    public void updateProfile(
            String githubEmail,
            String githubAvatarUrl,
            String githubName
    ) {
        this.githubEmail = githubEmail;
        this.githubAvatarUrl = githubAvatarUrl;
        this.githubName = githubName;
    }

    public void updateLastSyncedAt(LocalDateTime lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }

    private static void validate(Long githubUserId, String githubLogin) {
        if (githubUserId == null) {
            throw new IllegalArgumentException("GitHub User ID는 필수입니다.");
        }
        if (githubLogin == null || githubLogin.isBlank()) {
            throw new IllegalArgumentException("GitHub Login은 비어 있을 수 없습니다.");
        }
    }
}