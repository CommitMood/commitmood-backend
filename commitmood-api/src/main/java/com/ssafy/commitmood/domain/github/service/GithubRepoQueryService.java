package com.ssafy.commitmood.domain.github.service;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoListResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoResponse;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.repository.GithubRepoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GithubRepoQueryService {

    private final GithubRepoRepository repoRepository;

    public GithubRepoListResponse getUserRepos(Long userAccountId) {
        return GithubRepoListResponse.of(repoRepository.findByUserAccountId(userAccountId));
    }

    public GithubRepoResponse getRepo(Long repoId) {
        return repoRepository.findById(repoId)
                .map(GithubRepoResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("Repo not found: " + repoId));
    }

    public GithubRepoListResponse search(String keyword) {
        return GithubRepoListResponse.of(repoRepository.search(keyword));
    }

    public PageResponse<GithubRepoResponse> searchPaged(String keyword, int page, int size) {
        int offset = (page - 1) * size;

        List<GithubRepo> repos = repoRepository.searchPaged(keyword, size, offset);
        long totalCount = repoRepository.countByKeyword(keyword);

        List<GithubRepoResponse> content = repos.stream()
                .map(GithubRepoResponse::of)
                .toList();

        return PageResponse.of(content, page, size, totalCount);
    }

    // TODO Commit 도메인 개발 시 수정 필요
    public Object getCommitsByRepo(Long repoId) {
        throw new UnsupportedOperationException("Commit 조회 기능은 현재 개발 예정입니다.");
    }
}