package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper;
import com.ssafy.commitmood.domain.commit.repository.mapper.RankingMapper.RankingEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RankingRepositoryTest {

    @Mock
    private RankingMapper mapper;

    @InjectMocks
    private RankingRepository repository;

    private RankingEntry createMockEntry(Long userId, String login, int commitCount) {
        return new RankingEntry(userId, login, "", commitCount, 0, 0, 0.0, 0);
    }

    @Test
    @DisplayName("커밋 수 기준 랭킹 조회")
    void findRankingByCommitCount() {
        List<RankingEntry> mockEntries = List.of(
                createMockEntry(1L, "user1", 10),
                createMockEntry(2L, "user2", 5)
        );

        given(mapper.findRankingByCommitCount(10, 0))
                .willReturn(mockEntries);
        
        List<RankingEntry> result = repository.findRankingByCommitCount(10, 0);
        
        assertThat(result).hasSize(2);
        verify(mapper).findRankingByCommitCount(10, 0);
    }

    @Test
    @DisplayName("플래그 토큰 수 기준 랭킹 조회")
    void findRankingByFlaggedCount() {
        given(mapper.findRankingByFlaggedCount(anyInt(), anyInt()))
                .willReturn(List.of());

        repository.findRankingByFlaggedCount(10, 0);

        verify(mapper).findRankingByFlaggedCount(10, 0);
    }

    @Test
    @DisplayName("욕설 수 기준 랭킹 조회")
    void findRankingBySwearCount() {
        given(mapper.findRankingBySwearCount(anyInt(), anyInt()))
                .willReturn(List.of());

        repository.findRankingBySwearCount(10, 0);

        verify(mapper).findRankingBySwearCount(10, 0);
    }

    @Test
    @DisplayName("감정 점수 기준 랭킹 조회")
    void findRankingBySentimentScore() {
        given(mapper.findRankingBySentimentScore(anyInt(), anyInt()))
                .willReturn(List.of());

        repository.findRankingBySentimentScore(10, 0);

        verify(mapper).findRankingBySentimentScore(10, 0);
    }

    @Test
    @DisplayName("최근 활동 기준 랭킹 조회")
    void findRankingByRecent() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(30);
        given(mapper.findRankingByRecent(any(LocalDateTime.class), anyInt(), anyInt()))
                .willReturn(List.of());

        repository.findRankingByRecent(startDate, 10, 0);

        verify(mapper).findRankingByRecent(startDate, 10, 0);
    }

    @Test
    @DisplayName("전체 사용자 수 조회")
    void countTotalUsers() {
        given(mapper.countTotalUsers()).willReturn(100);

        int count = repository.countTotalUsers();

        assertThat(count).isEqualTo(100);
        verify(mapper).countTotalUsers();
    }
}
