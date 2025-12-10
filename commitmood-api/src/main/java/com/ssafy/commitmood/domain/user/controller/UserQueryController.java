package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User Query API",
        description = "사용자 조회 API (email, name, githubLogin 기반 Prefix 검색 제공)"
)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @Operation(
            summary = "사용자 조회 (쿼리 기반)",
            description = """
                    email / githubLogin / name 중 하나로 사용자 정보를 조회합니다. \n
                    · email & githubLogin 중 우선 적용됨 \n
                    · 파라미터가 없거나 name만 존재할 경우 → **name prefix 검색이 기본 동작**
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserAccountResponse.class))
    )
    @ApiResponse(responseCode = "404", description = "조회 결과 없음")
    @GetMapping
    public UserAccountResponse getUser(
            @Parameter(description = "사용자 이메일 Prefix")
            @RequestParam(required = false) String email,

            @Parameter(description = "GitHub 로그인 ID Prefix")
            @RequestParam(required = false) String githubLogin,

            @Parameter(description = "사용자 이름 Prefix (기본 검색 값)")
            @RequestParam(required = false) String name
    ) {
        if (email != null) {
            return userQueryService.getUserByEmail(email);
        }
        if (githubLogin != null) {
            return userQueryService.getUserByGithubLogin(githubLogin);
        }
        return userQueryService.getUserByName(name);
    }
}