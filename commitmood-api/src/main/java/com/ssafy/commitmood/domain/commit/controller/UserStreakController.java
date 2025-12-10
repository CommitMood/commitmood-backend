package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.UserStreakResponse;
import com.ssafy.commitmood.domain.commit.service.UserStreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("streak")
@RequiredArgsConstructor
public class UserStreakController {

    private final UserStreakService userStreakService;

    @GetMapping("/{userAccountId}")
    public ResponseEntity<UserStreakResponse> getUserStreak(
            @PathVariable Long userAccountId,
            @RequestParam(defaultValue = "month") String option
    ) {
        UserStreakResponse response = userStreakService.getUserStreak(userAccountId, option);
        return ResponseEntity.ok(response);
    }
}