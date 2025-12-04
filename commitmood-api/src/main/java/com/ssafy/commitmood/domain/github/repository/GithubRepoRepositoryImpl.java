package com.ssafy.commitmood.domain.github.repository;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.mapper.GithubRepoMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GithubRepoRepositoryImpl implements GithubRepoRepository {

    private final GithubRepoMapper mapper;

    public GithubRepoRepositoryImpl(GithubRepoMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public GithubRepo save(GithubRepo repo) {
        mapper.insert(repo);
        return repo;
    }

    @Override
    public Optional<GithubRepo> findById(Long repoId) {
        return mapper.findById(repoId);
    }

    @Override
    public List<GithubRepo> findByUserAccountId(Long userAccountId) {
        return mapper.findByUserAccountId(userAccountId);
    }

    @Override
    public List<GithubRepo> search(String keyword) {
        return mapper.searchByKeyword(keyword);
    }

    @Override
    public List<GithubRepo> searchPaged(String keyword, int pageSize, int offset) {
        return mapper.searchPaged(keyword, pageSize, offset);
    }

    @Override
    public void updateInfo(GithubRepo repo) {
        mapper.updateRepo(repo);
    }

    @Override
    public void updateLastSyncedAt(Long repoId, LocalDateTime time) {
        mapper.updateLastSyncedAt(repoId, time);
    }

    @Override
    public void deleteById(Long repoId) {
        mapper.deleteById(repoId);
    }

    @Override
    public long countByKeyword(String keyword) {
        return mapper.countByKeyword(keyword);
    }
}