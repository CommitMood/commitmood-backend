package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.repository.mapper.CommitStreakMapper;
import com.ssafy.commitmood.domain.commit.repository.mapper.CommitStreakMapper.DailyCommitCount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommitStreakRepositoryTest {
    @Mock
    private CommitStreakMapper mapper;

    @InjectMocks
    private CommitStreakRepository repository;

    @Test
    @DisplayName("특정 기간의 일별 커밋 수를 조회한다.")
    void findDailyCommitCounts_Success() {
        Long userAccountId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(6);
        LocalDate endDate = LocalDate.now();

        DailyCommitCount count1 = new DailyCommitCount(endDate.minusDays(1), 3);
        DailyCommitCount count2 = new DailyCommitCount(endDate, 4);

        List<DailyCommitCount> mockCounts = List.of(count1, count2);

        given(mapper.findDailyCommitCounts(userAccountId, startDate, endDate))
                .willReturn(mockCounts);

        List<DailyCommitCount> result = repository.findDailyCommitCounts(userAccountId, startDate, endDate);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().commitCount()).isEqualTo(3);
        verify(mapper).findDailyCommitCounts(userAccountId, startDate, endDate);
    }

    @Test
    @DisplayName("커밋이 없는 기간 조회 시 빈 리스트를 반환한다.")
    void findDailyCommitCounts_Empty() {
        Long userAccountId = 2L;
        LocalDate startDate = LocalDate.now().minusDays(6);
        LocalDate endDate = LocalDate.now();

        given(mapper.findDailyCommitCounts(userAccountId, startDate, endDate))
                .willReturn(List.of());

        List<DailyCommitCount> result = repository.findDailyCommitCounts(userAccountId, startDate, endDate);

        assertThat(result).isEmpty();
        verify(mapper).findDailyCommitCounts(userAccountId, startDate, endDate);
    }
}
