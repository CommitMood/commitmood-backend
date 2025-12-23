package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.auth.entity.AccessToken;
import com.ssafy.commitmood.domain.auth.entity.RefreshToken;
import com.ssafy.commitmood.security.config.AppProperties;
import com.ssafy.commitmood.security.jwt.JwtProperties;
import com.ssafy.commitmood.security.jwt.JwtTokenProvider;
import com.ssafy.commitmood.security.mapper.AccessTokenMapper;
import com.ssafy.commitmood.security.mapper.RefreshTokenMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final AppProperties appProperties;
    private final RefreshTokenMapper refreshTokenMapper;
    private final AccessTokenMapper accessTokenMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        var userAccount = oAuth2User.getUserAccount();

        // 1. Refresh Token 생성 (JWT)
        String refreshToken = jwtTokenProvider.generateRefreshToken(userAccount);

        // 1-1. Access Token 생성 (JWT)
        String accessToken = jwtTokenProvider.generateAccessToken(userAccount);

        // 2. Refresh Token DB 저장
        saveRefreshToken(userAccount.getId(), refreshToken);

        // 3. GitHub Access Token 저장 (서버 내부용)
        saveGithubAccessToken(
                userAccount.getId(),
                oAuth2User.getAccessToken()
        );

        // 4. Refresh Token을 HttpOnly Cookie로 전달
        addCookie(
                response,
                "refreshToken",
                refreshToken,
                (int) (jwtProperties.getRefreshTokenExpiry() / 1000)
        );

        // 5. 프론트엔드 OAuth Callback으로 redirect
        String redirectUrl = appProperties.frontendUrl()
                + "/oauth/callback"
                + "?accessToken=" + accessToken;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private void saveRefreshToken(Long userAccountId, String token) {
        refreshTokenMapper.deleteByUserAccountId(userAccountId);

        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtProperties.getRefreshTokenExpiry() / 1000);

        RefreshToken refreshToken = RefreshToken.create(
                userAccountId,
                token,
                expiresAt
        );
        refreshTokenMapper.insert(refreshToken);
    }

    private void saveGithubAccessToken(Long userAccountId, String githubToken) {
        accessTokenMapper.deleteByUserAccountId(userAccountId);

        AccessToken accessToken = AccessToken.create(
                userAccountId,
                githubToken,
                "BEARER",
                "read:user,user:email",
                null
        );
        accessTokenMapper.insert(accessToken);
    }

    private void addCookie(
            HttpServletResponse response,
            String name,
            String value,
            int maxAge
    ) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
