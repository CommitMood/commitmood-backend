package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.auth.entity.RefreshToken;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import com.ssafy.commitmood.security.jwt.JwtTokenProvider;
import com.ssafy.commitmood.security.mapper.RefreshTokenMapper;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final RefreshTokenMapper refreshTokenMapper;
    private final UserAccountMapper userAccountMapper;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Refresh Token을 검증하고 해당 토큰의 소유자인 UserAccount를 반환합니다.
     * <p>
     * - 토큰 존재 여부 확인 - 만료 여부 확인 - 사용자 조회
     */
    public UserAccount validateRefreshToken(String refreshTokenValue) {

        // 1. JWT 검증 및 userId 추출
        Long userAccountId = jwtTokenProvider.getUserIdFromToken(refreshTokenValue);

        // 2. DB에 refresh token 존재하는지 확인
        RefreshToken refreshToken = refreshTokenMapper.findByToken(refreshTokenValue)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        // 3. DB 만료 체크
        if (isExpired(refreshToken)) {
            refreshTokenMapper.deleteByToken(refreshTokenValue);
            throw new IllegalArgumentException("만료된 Refresh Token입니다.");
        }

        // 4. JWT subject와 DB userId 일치 여부 검증
        if (!refreshToken.getUserAccountId().equals(userAccountId)) {
            throw new IllegalArgumentException("Refresh Token 정보가 일치하지 않습니다.");
        }

        // 5. 사용자 조회
        return userAccountMapper.findById(userAccountId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    /**
     * 로그아웃 처리 - 해당 사용자의 모든 Refresh Token 제거
     */
    @Transactional
    public void logout(Long userAccountId) {
        refreshTokenMapper.deleteByUserAccountId(userAccountId);
    }

    private boolean isExpired(RefreshToken refreshToken) {
        return refreshToken.getExpiresAt() != null
                && refreshToken.getExpiresAt().isBefore(LocalDateTime.now());
    }
}
