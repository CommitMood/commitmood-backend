package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.common.exception.NotFoundException;
import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserAccountRepository repository;

    @Mock
    private UserAccount mockUser;

    @InjectMocks
    private UserQueryService service;

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void getUserById_success() {
        // given
        UserAccount user = UserAccount.create(
                1L,
                "devys",
                "test@test.com",
                "avatar",
                "Devys"
        );
        given(repository.findById(1L)).willReturn(Optional.of(user));

        // when
        UserAccountResponse response = service.getUserById(1L);

        // then
        assertThat(response.githubLogin()).isEqualTo("devys");
    }

    @Test
    @DisplayName("ID로 사용자 조회 실패 - NotFoundException 발생")
    void getUserById_notFound() {
        // given
        given(repository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.getUserById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("UserAccount not found");
    }

    @Test
    @DisplayName("GitHub Login으로 사용자 조회 성공")
    void getUserByGithubLogin_success() {
        // given
        UserAccount user = UserAccount.create(
                2L,
                "tester",
                "a@test.com",
                "avatar",
                "Tester"
        );
        given(repository.findByGithubLogin("tester")).willReturn(Optional.of(user));

        // when
        UserAccountResponse response = service.getUserByGithubLogin("tester");

        // then
        assertThat(response.githubLogin()).isEqualTo("tester");
    }

    @Test
    @DisplayName("GitHub Login으로 조회 실패 - NotFoundException")
    void getUserByGithubLogin_notFound() {
        // given
        given(repository.findByGithubLogin("unknown")).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.getUserByGithubLogin("unknown"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Entity 반환 조회 성공")
    void getEntityById_success() {
        // given
        UserAccount user = UserAccount.create(
                3L,
                "user3",
                "x@test.com",
                "avatar",
                "User3"
        );
        given(repository.findById(3L)).willReturn(Optional.of(user));

        // when
        UserAccount result = service.getEntityById(3L);

        // then
        assertThat(result.getGithubLogin()).isEqualTo("user3");
    }

    @Test
    @DisplayName("Entity 반환 실패 - NotFoundException 발생")
    void getEntityById_notFound() {
        // given
        given(repository.findById(5L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.getEntityById(5L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("existsById는 존재 여부만 반환한다")
    void existsById() {
        // given
        given(repository.findById(10L)).willReturn(Optional.of(mockUser));

        // when
        boolean exists = service.existsById(10L);

        // then
        assertThat(exists).isTrue();
    }
}