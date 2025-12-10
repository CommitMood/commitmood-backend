package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.service.UserCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User Command",
        description = "사용자 정보 생성 및 수정 Command API"
)
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final UserCommandService userCommandService;

    @Operation(
            summary = "GitHub 프로필 정보 업데이트",
            description = "내부 사용자 ID 기준 GithubProfileUpdateRequest 데이터를 전달 받아 사용자 GitHub 정보를 갱신한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 완료"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PatchMapping("/{id}/github/profile")
    public ResponseEntity<Void> updateGithubProfile(
            @Parameter(description = "수정 대상 내부 사용자 ID", example = "15")
            @PathVariable Long id,

            @Parameter(description = "GitHub 프로필 정보 변경 요청")
            @RequestBody GithubProfileUpdateRequest request
    ) {
        userCommandService.updateUserProfileFromGithub(id, request);
        return ResponseEntity.ok().build(); // 200 OK
    }

    @Operation(
            summary = "사용자 삭제",
            description = "내부 userId에 해당하는 사용자를 삭제한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 완료 (응답 Body 없음)"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "삭제 대상 사용자 ID", example = "15")
            @PathVariable Long id
    ) {
        userCommandService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}