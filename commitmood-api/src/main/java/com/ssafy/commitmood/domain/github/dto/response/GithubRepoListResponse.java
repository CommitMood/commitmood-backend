package com.ssafy.commitmood.domain.github.dto.response;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "깃허브 저장소 목록 응답 DTO")
public record GithubRepoListResponse(

        @Schema(description = "GitHub 저장소 리스트", example = """
                [
                    {
                        "id": 1,
                        "repoName": "commitmood-backend",
                        "repoUrl": "https://github.com/CommitMood/commitmood-backend"
                    }
                ]
                """)
        List<GithubRepoResponse> repoList,

        @Schema(description = "저장소 개수", example = "1")
        int count
) {

    public static GithubRepoListResponse of(List<GithubRepo> list) {
        return new GithubRepoListResponse(
                list.stream()
                        .map(GithubRepoResponse::of)
                        .toList(),
                list.size()
        );
    }
}