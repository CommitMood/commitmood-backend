package com.ssafy.commitmood.security.oauth;

import com.ssafy.commitmood.domain.auth.entity.AccessToken;
import com.ssafy.commitmood.domain.auth.entity.RefreshToken;
import com.ssafy.commitmood.security.mapper.AccessTokenMapper;
import com.ssafy.commitmood.security.mapper.RefreshTokenMapper;
import com.ssafy.commitmood.security.config.AppProperties;
import com.ssafy.commitmood.security.jwt.JwtProperties;
import com.ssafy.commitmood.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final AppProperties appProperties;
    private final RefreshTokenMapper refreshTokenMapper;
    private final AccessTokenMapper accessTokenMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String jwtAccessToken = jwtTokenProvider.generateAccessToken(oAuth2User.getUserAccount());
        String jwtRefreshToken = jwtTokenProvider.generateRefreshToken();

        saveRefreshToken(oAuth2User.getUserAccount().getId(), jwtRefreshToken);
        saveGithubAccessToken(oAuth2User.getUserAccount().getId(), oAuth2User.getAccessToken());

        addCookie(response, "accessToken", jwtAccessToken, (int) (jwtProperties.accessTokenExpiry() / 1000));
        addCookie(response, "refreshToken", jwtRefreshToken, (int) (jwtProperties.refreshTokenExpiry() / 1000));

        String redirectUrl = appProperties.frontendUrl() + "/oauth/callback";
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    private void saveRefreshToken(Long userAccountId, String token) {
        refreshTokenMapper.deleteByUserAccountId(userAccountId);

        LocalDateTime expiresAt = LocalDateTime.now()
                .plusSeconds(jwtProperties.refreshTokenExpiry() / 1000);
        RefreshToken refreshToken = RefreshToken.create(userAccountId, token, expiresAt);
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

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
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
