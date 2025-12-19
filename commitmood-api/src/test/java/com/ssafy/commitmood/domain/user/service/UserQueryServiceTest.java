package com.ssafy.commitmood.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.ssafy.commitmood.common.exception.NotFoundException;
import com.ssafy.commitmood.domain.user.dto.request.UserAccountQueryCondition;
import com.ssafy.commitmood.domain.user.dto.response.UserAccountResponse;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserAccountRepository repository;

    @InjectMocks
    private UserQueryService service;

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void getUserById_success() {
        UserAccount user = UserAccount.create(1L, "devys", "test@test.com", "avatar", "Devys");
        given(repository.findById(1L)).willReturn(Optional.of(user));

        UserAccountResponse response = service.getUserById(1L);

        assertThat(response.githubLogin()).isEqualTo("devys");
    }

    @Test
    @DisplayName("ID 조회 실패 - 예외 발생")
    void getUserById_notFound() {
        given(repository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserById(1L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Email Prefix 조회 성공")
    void getUserByEmail_success() {
        UserAccount user = UserAccount.create(2L, "user2", "mail@test.com", "a", "User2");
        given(repository.findByPrefixOne(new UserAccountQueryCondition("mail", null, null)))
                .willReturn(Optional.of(user));

        UserAccountResponse response = service.getUserByEmail("mail");

        assertThat(response.githubEmail()).startsWith("mail");
    }

    @Test
    @DisplayName("Email Prefix 조회 실패")
    void getUserByEmail_notFound() {
        given(repository.findByPrefixOne(new UserAccountQueryCondition("none", null, null)))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserByEmail("none"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("Name Prefix 조회 성공")
    void getUserByName_success() {
        UserAccount user = UserAccount.create(3L, "aaa", "a@test.com", "a", "예성");
        given(repository.findByPrefixOne(new UserAccountQueryCondition(null, "예", null)))
                .willReturn(Optional.of(user));

        UserAccountResponse response = service.getUserByName("예");

        assertThat(response.githubName()).startsWith("예");
    }

    @Test
    @DisplayName("Name Prefix 조회 실패")
    void getUserByName_notFound() {
        given(repository.findByPrefixOne(new UserAccountQueryCondition(null, "no", null)))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserByName("no"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("GitHubLogin Prefix 조회 성공")
    void getUserByGithubLogin_success() {
        UserAccount user = UserAccount.create(4L, "tester", "x@test.com", "a", "Tester");
        given(repository.findByPrefixOne(new UserAccountQueryCondition(null, null, "test")))
                .willReturn(Optional.of(user));

        UserAccountResponse response = service.getUserByGithubLogin("test");

        assertThat(response.githubLogin()).startsWith("test");
    }

    @Test
    @DisplayName("GitHubLogin Prefix 조회 실패")
    void getUserByGithubLogin_notFound() {
        given(repository.findByPrefixOne(new UserAccountQueryCondition(null, null, "none")))
                .willReturn(Optional.empty());

        assertThatThrownBy(() -> service.getUserByGithubLogin("none"))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("existsById true 반환")
    void existsById_true() {
        given(repository.findById(10L)).willReturn(Optional.of(UserAccount.create(
                10L, "u", "e", "a", "n"
        )));

        assertThat(service.existsById(10L)).isTrue();
    }

    @Test
    @DisplayName("existsById false 반환")
    void existsById_false() {
        given(repository.findById(10L)).willReturn(Optional.empty());

        assertThat(service.existsById(10L)).isFalse();
    }
}