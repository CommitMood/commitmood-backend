package com.ssafy.commitmood.repository.commit;

import com.ssafy.commitmood.domain.FlaggedToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FlaggedTokenMapper {

    void insert(FlaggedToken flaggedToken);

    void insertBatch(List<FlaggedToken> flaggedTokens);

    Optional<FlaggedToken> findById(@Param("id") Long id);

    List<FlaggedToken> findByCommitId(@Param("commitId") Long commitId);

    List<FlaggedToken> findByCommitIds(@Param("commitIds") List<Long> commitIds);

    void update(FlaggedToken flaggedToken);

    void deleteById(@Param("id") Long id);

    void deleteByCommitId(@Param("commitId") Long commitId);
}