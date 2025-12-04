package com.ssafy.commitmood.domain.github.mapper;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GithubRepoMapper {

    void insert(GithubRepo githubRepo);

    Optional<GithubRepo> findById(@Param("repoId") Long id);

    List<GithubRepo> findByUserAccountId(@Param("userAccountId") Long userAccountId);

    List<GithubRepo> searchByKeyword(@Param("keyword") String keyword);

    List<GithubRepo> searchPaged(
            @Param("keyword") String keyword,
            @Param("pageSize") int pageSize,
            @Param("offset") int offset
    );

    void updateRepo(GithubRepo repo);

    void updateLastSyncedAt(@Param("repoId") Long repoId, @Param("time") LocalDateTime time);

    void deleteById(@Param("repoId") Long repoId);

    long countByKeyword(String keyword);
}
