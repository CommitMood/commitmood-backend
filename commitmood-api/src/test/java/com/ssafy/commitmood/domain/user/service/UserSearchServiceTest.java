package com.ssafy.commitmood.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.user.dto.response.UserSearchResponse;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserSearchServiceTest {

    @Mock
    private UserAccountRepository repository;

    @InjectMocks
    private UserSearchService service;

    @Mock
    private UserAccount mockUser1;

    @Mock
    private UserAccount mockUser2;

    // -----------------------------
    //  Simple search(keyword only)
    // -----------------------------

    @Test
    @DisplayName("keyword가 null or blank이면 빈 리스트 반환")
    void search_blankKeyword_returnsEmpty() {
        assertThat(service.search(null)).isEmpty();
        assertThat(service.search("")).isEmpty();
        assertThat(service.search(" ")).isEmpty();
    }

    @Test
    @DisplayName("keyword로 검색 시 UserSearchResponse 리스트 반환")
    void search_success() {
        // given
        given(repository.searchByLoginLike("dev"))
                .willReturn(List.of(mockUser1, mockUser2));

        given(mockUser1.getGithubLogin()).willReturn("devys");
        given(mockUser2.getGithubLogin()).willReturn("developer");

        // when
        List<UserSearchResponse> results = service.search("dev");

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).githubLogin()).isEqualTo("devys");
        assertThat(results.get(1).githubLogin()).isEqualTo("developer");
    }

    // -----------------------------
    //  Paging search(keyword, page, size)
    // -----------------------------

    @Test
    @DisplayName("페이징 검색: content, totalCount, page, size가 올바르게 반환된다")
    void search_paged_success() {
        // given
        String keyword = "dev";
        int page = 2;
        int size = 10;
        int offset = (page - 1) * size;

        given(repository.searchByLoginPaged(keyword, size, offset))
                .willReturn(List.of(mockUser1, mockUser2));

        given(repository.countByLogin(keyword))
                .willReturn(25L); // total 25 → totalPages = 3

        given(mockUser1.getGithubLogin()).willReturn("devys");
        given(mockUser2.getGithubLogin()).willReturn("developer");

        // when
        PageResponse<UserSearchResponse> response =
                service.search(keyword, page, size);

        // then
        assertThat(response.page()).isEqualTo(2);
        assertThat(response.size()).isEqualTo(10);
        assertThat(response.totalCount()).isEqualTo(25);
        assertThat(response.totalPages()).isEqualTo(3);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.content()).hasSize(2);
    }
}