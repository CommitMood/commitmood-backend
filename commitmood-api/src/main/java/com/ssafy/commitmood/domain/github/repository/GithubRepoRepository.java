package com.ssafy.commitmood.domain.github.repository;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GithubRepoRepository {

    GithubRepo save(GithubRepo repo);

    Optional<GithubRepo> findById(Long repoId);

    List<GithubRepo> findByUserAccountId(Long userAccountId);

    List<GithubRepo> search(String keyword);

    List<GithubRepo> searchPaged(String keyword, int pageSize, int offset);

    void updateInfo(GithubRepo repo);

    void updateLastSyncedAt(Long repoId, LocalDateTime time);

    void deleteById(Long repoId);

    long countByKeyword(String keyword);
}