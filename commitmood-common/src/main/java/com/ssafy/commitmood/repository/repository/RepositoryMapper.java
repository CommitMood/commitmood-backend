package com.ssafy.commitmood.repository.repository;

import com.ssafy.commitmood.domain.Repository;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RepositoryMapper {

    void insert(Repository repository);

    Optional<Repository> findById(@Param("id") Long id);

    Optional<Repository> findByGithubRepoId(@Param("githubRepoId") Long githubRepoId);

    List<Repository> findByOwnerId(@Param("ownerId") Long ownerId);

    List<Repository> findAll();

    void update(Repository repository);

    void deleteById(@Param("id") Long id);

    void deleteByOwnerId(@Param("ownerId") Long ownerId);
}