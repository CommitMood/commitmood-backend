package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.auth.entity.RefreshToken;
import com.ssafy.commitmood.security.jwt.JwtProperties;
import com.ssafy.commitmood.security.jwt.JwtTokenProvider;
import com.ssafy.commitmood.security.mapper.RefreshTokenMapper;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
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
    private final JwtProperties jwtProperties;

    @Transactional
    public String refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenMapper.findByToken(refreshTokenValue)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 Refresh Token입니다."));

        if (refreshToken.isExpired()) {
            refreshTokenMapper.deleteByToken(refreshTokenValue);
            throw new IllegalArgumentException("만료된 Refresh Token입니다.");
        }

        UserAccount userAccount = userAccountMapper.findById(refreshToken.getUserAccountId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return jwtTokenProvider.generateAccessToken(userAccount);
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenMapper.deleteByUserAccountId(userId);
    }

    public UserAccount getCurrentUser(Long userId) {
        return userAccountMapper.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
