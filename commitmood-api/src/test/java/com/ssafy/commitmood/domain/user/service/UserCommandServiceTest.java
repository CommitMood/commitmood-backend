package com.ssafy.commitmood.domain.user.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserAccountRepository repository;

    @Mock
    private UserAccount user;

    @InjectMocks
    private UserCommandService service;

    @Test
    @DisplayName("GitHub 프로필 기반 사용자 정보 업데이트 성공")
    void updateUserProfileFromGithub_success() {
        // given
        Long userId = 1L;
        GithubProfileUpdateRequest request = new GithubProfileUpdateRequest(
                "new@mail.com", "avatar.png", "New Name"
        );
        given(repository.findById(userId)).willReturn(Optional.of(user));

        // when & then
        assertThatCode(() -> service.updateUserProfileFromGithub(userId, request))
                .doesNotThrowAnyException();

        then(user).should().updateProfile("new@mail.com", "avatar.png", "New Name");
        then(repository).should().update(eq(user));
    }


    @Test
    @DisplayName("마지막 동기화 시간 업데이트 성공")
    void updateLastSyncedAt_success() {
        Long userId = 10L;
        LocalDateTime now = LocalDateTime.now();

        given(repository.findById(userId)).willReturn(Optional.of(user));

        service.updateLastSyncedAt(userId, now);

        then(repository).should().updateLastSyncedAt(userId, now);
    }


    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_success() {
        Long userId = 99L;
        given(repository.findById(userId)).willReturn(Optional.of(user));

        service.deleteUser(userId);

        then(repository).should().deleteById(userId);
    }
}