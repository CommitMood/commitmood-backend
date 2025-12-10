package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.domain.user.dto.request.GithubProfileUpdateRequest;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserAccountRepository repository;

    @Mock
    private UserQueryService queryService;

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

        given(queryService.getEntityById(userId)).willReturn(user);

        // when & then
        assertThatCode(() -> service.updateUserProfileFromGithub(userId, request))
                .doesNotThrowAnyException();

        then(user).should().updateProfile(
                "new@mail.com",
                "avatar.png",
                "New Name"
        );
        then(repository).should().save(user);
    }

    @Test
    @DisplayName("마지막 동기화 시간 업데이트 성공")
    void updateLastSyncedAt_success() {
        // given
        Long userId = 10L;
        LocalDateTime now = LocalDateTime.now();

        given(queryService.getEntityById(userId)).willReturn(user);

        // when
        service.updateLastSyncedAt(userId, now);

        // then
        then(repository).should().updateLastSyncedAt(userId, now);
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void deleteUser_success() {
        // given
        Long userId = 99L;
        given(queryService.getEntityById(userId)).willReturn(user);

        // when
        service.deleteUser(userId);

        // then
        then(repository).should().deleteById(userId);
    }
}