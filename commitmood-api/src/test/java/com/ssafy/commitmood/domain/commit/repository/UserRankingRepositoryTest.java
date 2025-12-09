package com.ssafy.commitmood.domain.commit.repository;

import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper;
import com.ssafy.commitmood.domain.commit.repository.mapper.UserRankingMapper.UserRankingDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserRankingRepositoryTest {

    @Mock
    private UserRankingMapper mapper;

    @InjectMocks
    private UserRankingRepository repository;

    private UserRankingDetail createMockDetail(Long commitLogId, String repoName, Long flaggedCount) {
        return new UserRankingDetail(commitLogId, repoName, "test", "2025-12-09T10:00:00", flaggedCount, 0L, 0L, 0.0, "");
    }

    @Test
    @DisplayName("저장소별 사용자 랭킹 조회")
    void findUserRankingByRepo() {
        Long userAccountId = 1L;
        List<UserRankingDetail> mockDetails = List.of(
                createMockDetail(1L, "repo1", 5L),
                createMockDetail(2L, "repo2", 3L)
        );

        given(mapper.findUserRankingByRepo(userAccountId, 10, 0))
                .willReturn(mockDetails);

        List<UserRankingDetail> result = repository.findUserRankingByRepo(userAccountId, 10, 0);

        assertThat(result).hasSize(2);
        verify(mapper).findUserRankingByRepo(userAccountId, 10, 0);
    }

    @Test
    @DisplayName("플래그별 사용자 랭킹 조회")
    void findUserRankingByFlagged() {
        Long userAccountId = 1L;
        given(mapper.findUserRankingByFlagged(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());

        repository.findUserRankingByFlagged(userAccountId, 10, 0);

        verify(mapper).findUserRankingByFlagged(userAccountId, 10, 0);
    }

    @Test
    @DisplayName("감정별 사용자 랭킹 조회")
    void findUserRankingBySentiment() {
        Long userAccountId = 1L;
        given(mapper.findUserRankingBySentiment(eq(userAccountId), anyInt(), anyInt()))
                .willReturn(List.of());

        repository.findUserRankingBySentiment(userAccountId, 10, 0);

        verify(mapper).findUserRankingBySentiment(userAccountId, 10, 0);
    }

    @Test
    @DisplayName("저장소별 커밋 카운트 조회")
    void countUserRankingByRepo() {
        Long userAccountId = 1L;
        given(mapper.countUserRankingByRepo(userAccountId)).willReturn(10);
        
        int count = repository.countUserRankingByRepo(userAccountId);
        
        assertThat(count).isEqualTo(10);
        verify(mapper).countUserRankingByRepo(userAccountId);
    }

    @Test
    @DisplayName("플래그별 커밋 카운트 조회")
    void countUserRankingByFlagged() {
        Long userAccountId = 1L;
        given(mapper.countUserRankingByFlagged(userAccountId)).willReturn(5);
        
        int count = repository.countUserRankingByFlagged(userAccountId);

        assertThat(count).isEqualTo(5);
        verify(mapper).countUserRankingByFlagged(userAccountId);
    }

    @Test
    @DisplayName("감정별 커밋 카운트 조회")
    void countUserRankingBySentiment() {
        Long userAccountId = 1L;
        given(mapper.countUserRankingBySentiment(userAccountId)).willReturn(8);
        
        int count = repository.countUserRankingBySentiment(userAccountId);
        
        assertThat(count).isEqualTo(8);
        verify(mapper).countUserRankingBySentiment(userAccountId);
    }
}
