package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.user.dto.response.UserSearchResponse;
import com.ssafy.commitmood.domain.user.service.UserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "User Search",
        description = "사용자 검색 API (키워드 기반 일반/페이징 검색)"
)
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/search")
public class UserSearchController {

    private final UserSearchService searchService;

    @Operation(
            summary = "사용자 검색",
            description = "keyword 기반으로 사용자 리스트를 검색한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "검색 성공",
                    content = @Content(schema = @Schema(implementation = UserSearchResponse.class))),
            @ApiResponse(responseCode = "400", description = "keyword 파라미터 누락 또는 형식 오류"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping
    public List<UserSearchResponse> search(
            @Parameter(description = "검색 키워드 (유저명 / 이메일 등)", example = "yeseong")
            @RequestParam String keyword
    ) {
        return searchService.search(keyword);
    }

    @Operation(
            summary = "사용자 검색 (페이징)",
            description = "keyword 기반으로 페이지네이션된 사용자 검색 결과를 반환한다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "페이징 검색 성공",
                    content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "400", description = "page/size 파라미터 오류 또는 keyword 누락"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/page")
    public PageResponse<UserSearchResponse> searchPaged(
            @Parameter(description = "검색 키워드", example = "commit")
            @RequestParam String keyword,

            @Parameter(description = "페이지 번호 (1부터 시작)", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        return searchService.search(keyword, page, size);
    }
}