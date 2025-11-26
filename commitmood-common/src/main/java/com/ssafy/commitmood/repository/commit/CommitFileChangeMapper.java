package com.ssafy.commitmood.repository.commit;

import com.ssafy.commitmood.domain.CommitFileChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Optional;

@Mapper
public interface CommitFileChangeMapper {

    void insert(CommitFileChange commitFileChange);

    void insertBatch(List<CommitFileChange> commitFileChanges);

    Optional<CommitFileChange> findById(@Param("id") Long id);

    List<CommitFileChange> findByCommitId(@Param("commitId") Long commitId);

    void update(CommitFileChange commitFileChange);

    void deleteById(@Param("id") Long id);

    void deleteByCommitId(@Param("commitId") Long commitId);
}