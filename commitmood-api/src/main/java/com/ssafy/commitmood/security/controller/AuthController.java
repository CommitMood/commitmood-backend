package com.ssafy.commitmood.security.controller;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.security.jwt.JwtProperties;
import com.ssafy.commitmood.security.jwt.JwtTokenProvider;
import com.ssafy.commitmood.security.oauth.AuthMeResponse;
import com.ssafy.commitmood.security.oauth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    /**
     * Refresh Token 기반 Access Token 재발급
     */
    @Operation(
            summary = "Access Token 재발급",
            description = "HttpOnly Cookie에 저장된 Refresh Token을 사용하여 Access Token을 재발급합니다."
    )
    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> issueAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(401).build();
        }

        try {
            // 1. Refresh Token 검증 및 사용자 조회
            UserAccount userAccount = authService.validateRefreshToken(refreshToken);

            // 2. Access Token 발급
            String accessToken = jwtTokenProvider.generateAccessToken(userAccount);

            // 3. JSON Body로 Access Token 반환 (프론트 연동 안정성)
            return ResponseEntity.ok(
                    Map.of("accessToken", accessToken)
            );

        } catch (IllegalArgumentException e) {
            // Refresh Token이 유효하지 않으면 쿠키 제거
            clearRefreshTokenCookie(response);
            return ResponseEntity.status(401).build();
        }
    }

    /**
     * 현재 로그인 사용자 정보 조회
     */
    @Operation(
            summary = "현재 사용자 정보 조회",
            description = "JWT 인증이 완료된 현재 사용자의 정보를 반환합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<AuthMeResponse> getCurrentUser(
            @AuthenticationPrincipal UserAccount userAccount
    ) {
        if (userAccount == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(AuthMeResponse.from(userAccount));
    }

    /**
     * 로그아웃
     */
    @Operation(
            summary = "로그아웃",
            description = "Refresh Token을 DB 및 Cookie에서 제거합니다."
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserAccount userAccount,
            HttpServletResponse response
    ) {
        if (userAccount != null) {
            authService.logout(userAccount.getId());
        }

        clearRefreshTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    /**
     * Refresh Token 쿠키 제거
     */
    private void clearRefreshTokenCookie(HttpServletResponse response) {
        boolean secure = jwtProperties.isSecure(); // local=false, prod=true

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite(secure ? "None" : "Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
