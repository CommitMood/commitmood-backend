package com.ssafy.commitmood.service;

import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.mapper.CommitLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommitLogService {
    private final CommitLogMapper commitLogMapper;

    public void insert(CommitLog commitLog) {
        commitLogMapper.insert(commitLog);
    }

    public Optional<CommitLog> findById(long id) {
        return commitLogMapper.findById(id);
    }

    public List<CommitLog> findAllByGithubRepoId(long githubRepoId) {
        return commitLogMapper.findAllByGithubRepoId(githubRepoId);
    }
}
