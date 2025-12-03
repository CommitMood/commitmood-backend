package com.ssafy.commitmood.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserAccountRepository.class)
class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository repository;

    @Autowired
    private UserAccountMapper mapper;

    @Test
    @DisplayName("UserAccount 저장 후 조회 테스트")
    public void saveAndFind() {
        // given
        UserAccount user = UserAccount.create(
                100L,
                "devys",
                "dev@test.com",
                "avatar",
                "Dev"
        );

        // when
        repository.save(user);

        Optional<UserAccount> result = repository.findById(user.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getGithubLogin()).isEqualTo("devys");
    }

    @Test
    @DisplayName("GitHub Login으로 조회 테스트")
    public void findByGithubLogin() {
        // given
        UserAccount user = UserAccount.create(
                200L,
                "helloUser",
                "hello@test.com",
                "avatar",
                "Hello"
        );

        // when
        repository.save(user);

        Optional<UserAccount> found = repository.findByGithubLogin("helloUser");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getGithubLogin()).isEqualTo("helloUser");
    }

    @Test
    @DisplayName("프로필 업데이트 테스트")
    public void updateProfile() {
        // given
        UserAccount user = UserAccount.create(
                300L,
                "tester",
                "old@test.com",
                "oldAvatar",
                "OldName"
        );

        // when
        repository.save(user);

        user.updateProfile("new@test.com", "newAvatar", "NewName");

        repository.update(user);

        UserAccount updated = repository.findById(user.getId()).orElseThrow();

        // then
        assertThat(updated.getGithubEmail()).isEqualTo("new@test.com");
        assertThat(updated.getGithubAvatarUrl()).isEqualTo("newAvatar");
        assertThat(updated.getGithubName()).isEqualTo("NewName");
    }

    @Test
    @DisplayName("lastSyncedAt 업데이트 테스트")
    void updateLastSyncedAt() {
        // given
        UserAccount user = UserAccount.create(
                400L,
                "syncUser",
                "sync@test.com",
                "avatar",
                "Sync"
        );

        // when
        repository.save(user);

        LocalDateTime now = LocalDateTime.now();

        repository.updateLastSyncedAt(user.getId(), now);

        UserAccount updated = repository.findById(user.getId()).orElseThrow();

        // then
        assertThat(updated.getLastSyncedAt()).isEqualTo(now);
    }
}