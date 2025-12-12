package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;

public interface FlaggedTokenRepository {

    FlaggedToken save(FlaggedToken token);
}