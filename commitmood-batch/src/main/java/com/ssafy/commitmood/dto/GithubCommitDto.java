package com.ssafy.commitmood.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GithubCommitDto {
    private String sha;  // github_commit_sha

    @JsonProperty("html_url")
    private String htmlUrl;

    private CommitDetailDto commit;

    private AuthorDto author;  // GitHub 사용자 정보

    private StatsDto stats;  // additions, deletions 정보

    @Data
    public static class CommitDetailDto {
        private AuthorInfoDto author;  // 커밋 작성자 정보
        private String message;

        @JsonProperty("comment_count")
        private int commentCount;
    }

    @Data
    public static class AuthorInfoDto {
        private String name;
        private String email;
        private LocalDateTime date;  // committed_at
    }

    @Data
    public static class AuthorDto {
        private Long id;  // author_id
        private String login;

        @JsonProperty("avatar_url")
        private String avatarUrl;
    }

    @Data
    public static class StatsDto {
        private Integer additions;
        private Integer deletions;
        private Integer total;  // total_changes
    }
}