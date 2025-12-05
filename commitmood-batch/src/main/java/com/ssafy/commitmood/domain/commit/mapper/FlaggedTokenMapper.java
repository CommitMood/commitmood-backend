package com.ssafy.commitmood.domain.commit.mapper;

import java.util.List;
import java.util.Optional;

import com.ssafy.commitmood.domain.commit.entity.FlaggedToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FlaggedTokenMapper {

    /**
     * Flagged Token 저장
     */
    void insert(FlaggedToken flaggedToken);

    /**
     * Flagged Token 업데이트
     */
    int update(FlaggedToken flaggedToken);

    /**
     * ID로 Flagged Token 조회
     */
    Optional<FlaggedToken> findById(@Param("id") Long id);

    /**
     * Commit Log ID로 모든 Flagged Token 조회
     */
    List<FlaggedToken> findAllByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * Commit Log ID와 Token Type으로 조회
     */
    List<FlaggedToken> findAllByCommitLogIdAndTokenType(
            @Param("commitLogId") Long commitLogId,
            @Param("tokenType") FlaggedToken.TokenType tokenType
    );

    /**
     * Commit Log ID, Token, Token Type으로 조회 (유니크 제약)
     */
    Optional<FlaggedToken> findByCommitLogIdAndTokenAndType(
            @Param("commitLogId") Long commitLogId,
            @Param("token") String token,
            @Param("tokenType") FlaggedToken.TokenType tokenType
    );

    /**
     * Flagged Token 삭제
     */
    int deleteById(@Param("id") Long id);

    /**
     * Commit Log ID로 모든 Flagged Token 삭제
     */
    int deleteAllByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * Commit Log ID의 Flagged Token 개수 조회
     */
    int countByCommitLogId(@Param("commitLogId") Long commitLogId);

    /**
     * 일괄 저장
     */
    void insertBatch(@Param("flaggedTokens") List<FlaggedToken> flaggedTokens);
}