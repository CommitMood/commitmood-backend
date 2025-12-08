package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.user.dto.response.UserSearchResponse;
import com.ssafy.commitmood.domain.user.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/search")
public class UserSearchController {

    private final UserSearchService searchService;

    @GetMapping
    public List<UserSearchResponse> search(
            @RequestParam String keyword
    ) {
        return searchService.search(keyword);
    }

    @GetMapping("/page")
    public PageResponse<UserSearchResponse> searchPaged(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return searchService.search(keyword, page, size);
    }
}