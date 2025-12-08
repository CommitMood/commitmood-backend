package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.repository.mapper.FlaggedTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FlaggedTokenRepository {

    private final FlaggedTokenMapper mapper;

    public List<FlaggedToken> findAllByCommitLogId(Long commitLogId) {
        return mapper.findAllByCommitLogId(commitLogId);
    }
}