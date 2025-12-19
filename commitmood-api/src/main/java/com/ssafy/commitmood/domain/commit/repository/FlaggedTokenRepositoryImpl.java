package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import com.ssafy.commitmood.domain.commit.mapper.FlaggedTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FlaggedTokenRepositoryImpl implements FlaggedTokenRepository {

    private final FlaggedTokenMapper mapper;

    @Override
    public FlaggedToken save(FlaggedToken token) {
        mapper.insert(token);
        return token;
    }
}