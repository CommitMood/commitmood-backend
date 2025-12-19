package com.ssafy.commitmood.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GithubRepoDto {
    private Long id;  // github_repo_id로 매핑될 것

    private String name;

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("default_branch")
    private String defaultBranch;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("private")
    private boolean isPrivate;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @JsonProperty("description")
    private String description;

    private OwnerDto owner;

    @Data
    public static class OwnerDto {
        private Long id;
        private String login;
    }
}