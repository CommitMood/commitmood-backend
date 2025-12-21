package com.ssafy.commitmood.security.controller;

import com.ssafy.commitmood.security.jwt.JwtProperties;
import com.ssafy.commitmood.security.oauth.AuthMeResponse;
import com.ssafy.commitmood.security.oauth.AuthService;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @Operation(summary = "Access Token 갱신", description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 갱신 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 Refresh Token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            String newAccessToken = authService.refreshAccessToken(refreshToken);
            addCookie(response, "accessToken", newAccessToken, (int) (jwtProperties.accessTokenExpiry() / 1000));
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            clearCookies(response);
            return ResponseEntity.status(401).build();
        }
    }

    @Operation(summary = "로그아웃", description = "현재 사용자의 세션을 종료하고 토큰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal UserAccount userAccount,
            HttpServletResponse response) {

        if (userAccount != null) {
            authService.logout(userAccount.getId());
        }

        clearCookies(response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "현재 사용자 정보 조회", description = "인증된 현재 사용자의 정보를 반환합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = AuthMeResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/me")
    public ResponseEntity<AuthMeResponse> getCurrentUser(
            @AuthenticationPrincipal UserAccount userAccount) {

        if (userAccount == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(AuthMeResponse.from(userAccount));
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

    private void clearCookies(HttpServletResponse response) {
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }
}
