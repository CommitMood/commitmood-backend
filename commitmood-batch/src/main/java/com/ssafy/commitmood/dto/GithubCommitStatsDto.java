package com.ssafy.commitmood.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GithubCommitStatsDto {
    private StatsDto stats;

    private List<FilesDto> files;

    @Data
    public static class StatsDto {
        private Long additions;
        private Long deletions;
        private Long total;  // additions + deletions
    }

    @Data
    public static class FilesDto {
        private String sha;
        private String filename;
        private String status;
        private Long additions;
        private Long deletions;
        private Long changes;

        @JsonProperty("blob_url")
        private String blobUrl;

        @JsonProperty("raw_url")
        private String rawUrl;

        @JsonProperty("contents_url")
        private String contentsUrl;

        private String patch;
    }
}
