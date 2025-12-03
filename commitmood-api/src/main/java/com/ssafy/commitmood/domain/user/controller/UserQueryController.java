package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/{id}")
    public UserAccountResponse getUserByLogin(@PathVariable Long id) {
        return userQueryService.getUserById(id);
    }

    @GetMapping("/login/{githubLogin}")
    public UserAccountResponse getUserByLogin(@PathVariable String githubLogin) {
        return userQueryService.getUserByGithubLogin(githubLogin);
    }
}
