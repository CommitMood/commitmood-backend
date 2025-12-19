package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.mapper.CommitLogMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommitLogRepositoryImpl implements CommitLogRepository {

    private final CommitLogMapper mapper;

    @Override
    public void save(CommitLog commitLog) {
        mapper.insert(commitLog);
    }

    @Override
    public List<CommitLog> findByRepoId(Long repoId) {
        return mapper.findByRepoId(repoId);
    }
}