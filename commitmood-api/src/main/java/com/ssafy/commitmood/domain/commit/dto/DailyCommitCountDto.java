package com.ssafy.commitmood.domain.commit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 일별 커밋 수 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DailyCommitCountDto {
    private LocalDate commitDate;
    private Long commitCount;
}