package com.ssafy.commitmood.domain.commit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.commit.dto.DailyCommitCountDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@Import(UserStreakRepositoryImpl.class)
class UserStreakRepositoryTest {

    @Autowired
    private UserStreakRepository userStreakRepository;

    @Test
    @DisplayName("특정 기간 동안 사용자의 일별 커밋 수를 조회한다")
    void findDailyCommitCounts() {
        // given
        Long userAccountId = 1L;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        // when
        List<DailyCommitCountDto> result =
                userStreakRepository.findDailyCommitCounts(
                        userAccountId,
                        startDate,
                        endDate
                );

        // then
        assertThat(result).isNotNull();
        assertThat(result).allSatisfy(dto -> {
            assertThat(dto.getCommitDate()).isNotNull();
            assertThat(dto.getCommitCount()).isNotNull();
            assertThat(dto.getCommitCount()).isGreaterThanOrEqualTo(0);
        });
    }

    @Test
    @DisplayName("특정 기간 동안 사용자의 총 커밋 일수를 조회한다")
    void countTotalCommitDays() {
        // given
        Long userAccountId = 1L;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 1, 31);

        // when
        Integer totalCommitDays =
                userStreakRepository.countTotalCommitDays(
                        userAccountId,
                        startDate,
                        endDate
                );

        // then
        assertThat(totalCommitDays).isNotNull();
        assertThat(totalCommitDays).isGreaterThanOrEqualTo(0);
    }
}