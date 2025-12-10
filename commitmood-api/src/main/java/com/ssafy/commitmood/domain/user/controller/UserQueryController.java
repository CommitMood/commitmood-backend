package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping
    public UserAccountResponse getUser(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String githubLogin
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
