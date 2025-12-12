package com.ssafy.commitmood.domain.github.service;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.mapper.CommitLogMapper;
import com.ssafy.commitmood.domain.github.dto.response.GithubCommitResponse;
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

    private final GithubRepoRepository githubRepoRepository;
    //    TODO Commit 도메인쪽 repository 구현체/인터페이스 통일 필요
    //    private final CommitLogRepository commitLogRepository;
    private final CommitLogMapper commitLogMapper;

    public GithubRepoListResponse getUserRepos(Long userAccountId) {
        return GithubRepoListResponse.of(githubRepoRepository.findByUserAccountId(userAccountId));
    }

    public GithubRepoResponse getRepo(Long repoId) {
        return githubRepoRepository.findById(repoId)
                .map(GithubRepoResponse::of)
                .orElseThrow(() -> new IllegalArgumentException("Repo not found: " + repoId));
    }

    public GithubRepoListResponse search(String keyword) {
        return GithubRepoListResponse.of(githubRepoRepository.search(keyword));
    }

    public PageResponse<GithubRepoResponse> searchPaged(String keyword, int page, int size) {
        int offset = (page - 1) * size;

        List<GithubRepo> repos = githubRepoRepository.searchPaged(keyword, size, offset);
        long totalCount = githubRepoRepository.countByKeyword(keyword);

        List<GithubRepoResponse> content = repos.stream()
                .map(GithubRepoResponse::of)
                .toList();

        return PageResponse.of(content, page, size, totalCount);
    }

    public List<GithubCommitResponse> getCommitsByRepo(Long repoId) {
        List<CommitLog> logs = commitLogMapper.findByRepoId(repoId);

        return logs.stream()
                .map(GithubCommitResponse::of)
                .toList();
    }
}