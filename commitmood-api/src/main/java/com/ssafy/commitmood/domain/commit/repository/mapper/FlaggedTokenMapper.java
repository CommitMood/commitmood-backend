package com.ssafy.commitmood.domain.commit.repository.mapper;

import java.util.List;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FlaggedTokenMapper {

    /**
     * 특정 커밋의 모든 플래그 토큰 조회
     * @param commitLogId COMMIT_LOG.COMMIT_LOG_ID
     * @return 플래그 토큰 목록
     */
    List<FlaggedToken> findAllByCommitLogId(Long commitLogId);
}