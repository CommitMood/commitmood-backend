package com.ssafy.commitmood.domain.commit.controller;

import com.ssafy.commitmood.domain.commit.dto.response.StreakResponse;
import com.ssafy.commitmood.domain.commit.service.UserStreakQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserStreakQueryController {
    private final UserStreakQueryService service;

    @GetMapping("/{userAccountId}/streak")
    public StreakResponse getUserStreak(
            @PathVariable Long userAccountId,
            @RequestParam(required = false, defaultValue = "week") String option
    ) {
        return service.getStreakByUserId(userAccountId, option);
    }
}
