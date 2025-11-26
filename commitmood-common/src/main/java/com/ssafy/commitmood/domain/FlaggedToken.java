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
public class FlaggedToken {
    private Long id;
    private Long commitId;
    private String token;
    private TokenType tokenType;
    private Integer weight;
    private LocalDateTime createdAt;

    public enum TokenType {
        SWEAR, SLANG, EMOJI, EMPHASIS, OTHER
    }
}