package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import java.util.List;

public interface CommitLogRepository {

    void save(CommitLog commitLog);

    List<CommitLog> findByRepoId(Long repoId);
}