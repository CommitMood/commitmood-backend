package com.ssafy.commitmood.domain.commit.service;

import com.ssafy.commitmood.domain.commit.dto.response.FlaggedTokenListResponse;
import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.repository.FlaggedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommitTokenQueryService {

    private final FlaggedTokenRepository flaggedTokenRepository;

    public FlaggedTokenListResponse getTokensByCommitLogId(Long commitLogId) {
        List<FlaggedToken> tokens = flaggedTokenRepository.findAllByCommitLogId(commitLogId);
        return FlaggedTokenListResponse.from(commitLogId, tokens);
    }
}