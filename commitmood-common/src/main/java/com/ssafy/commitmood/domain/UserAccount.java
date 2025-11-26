package com.ssafy.commitmood.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {
    private Long userId;
    private String githubId;
    private String githubLogin;
    private String githubEmail;
    private String githubAvatarUrl;
    private String githubName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
