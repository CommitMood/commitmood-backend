package com.ssafy.commitmood.domain.user.controller;

import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.service.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final UserCommandService userCommandService;

    @PatchMapping("/{id}/profile")
    public void updateProfile(
            @PathVariable Long id,
            @RequestBody GithubProfileUpdateRequest request
    ) {
        userCommandService.updateUserProfileFromGithub(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userCommandService.deleteUser(id);
    }
}
