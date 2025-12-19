package com.ssafy.commitmood.domain.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.user.dto.request.UserAccountQueryCondition;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserAccountRepositoryImpl.class)
class UserAccountRepositoryTest {

    @Autowired
    UserAccountRepository repository;

    @Test
    @DisplayName("UserAccount 저장 후 단건 조회 (ID 기반)")
    void saveAndFind() {
        // given
        UserAccount user = UserAccount.create(100L, "devys", "dev@test.com", "avatar", "Dev");
        repository.save(user);

        // when
        Optional<UserAccount> result = repository.findById(user.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getGithubLogin()).isEqualTo("devys");
    }

    @Test
    @DisplayName("GithubLogin Prefix 조회 (findByPrefixOne)")
    void findByGithubLoginPrefix() {
        // given
        repository.save(UserAccount.create(201L, "helloUser", "h1@test.com", null, "Hello"));
        repository.save(UserAccount.create(202L, "helloDev", "h2@test.com", null, "Hello Dev"));

        // when
        UserAccountQueryCondition condition = new UserAccountQueryCondition(null, null, "hello");
        Optional<UserAccount> found = repository.findByPrefixOne(condition);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getGithubLogin()).startsWith("hello");
    }

    @Test
    @DisplayName("Email Prefix 조회 (findByPrefixOne)")
    void findByEmailPrefix() {
        // given
        repository.save(UserAccount.create(301L, "u1", "email@test.com", null, "E1"));
        repository.save(UserAccount.create(302L, "u2", "email2@test.com", null, "E2"));

        // when
        UserAccountQueryCondition condition = new UserAccountQueryCondition("email", null, null);
        Optional<UserAccount> found = repository.findByPrefixOne(condition);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getGithubEmail()).startsWith("email");
    }

    @Test
    @DisplayName("Name Prefix 목록 조회 (findAllByPrefix)")
    void findAllByNamePrefix() {
        // given
        repository.save(UserAccount.create(401L, "yeseong31", "a@test.com", null, "예성"));
        repository.save(UserAccount.create(402L, "yesman", "b@test.com", null, "예스맨"));
        repository.save(UserAccount.create(403L, "nope", "c@test.com", null, "노"));

        // when
        UserAccountQueryCondition condition = new UserAccountQueryCondition(null, "예", null);
        List<UserAccount> result = repository.findAllByPrefix(condition);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGithubName()).startsWith("예");
        assertThat(result.get(1).getGithubName()).startsWith("예");
    }

    @Test
    @DisplayName("Profile 업데이트 테스트")
    void updateProfile() {
        // given
        UserAccount user = UserAccount.create(500L, "tester", "old@test.com", "oldA", "Old");
        repository.save(user);

        // when
        user.updateProfile("new@test.com", "newA", "New");
        repository.update(user);

        // then
        UserAccount updated = repository.findById(user.getId()).orElseThrow();
        assertThat(updated.getGithubEmail()).isEqualTo("new@test.com");
        assertThat(updated.getGithubAvatarUrl()).isEqualTo("newA");
        assertThat(updated.getGithubName()).isEqualTo("New");
    }

    @Test
    @DisplayName("lastSyncedAt 업데이트 테스트")
    void updateLastSyncedAt() {
        UserAccount user = UserAccount.create(600L, "sync", "sync@test.com", "a", "Sync");
        repository.save(user);

        LocalDateTime now = LocalDateTime.now();
        repository.updateLastSyncedAt(user.getId(), now);

        assertThat(repository.findById(user.getId()).orElseThrow().getLastSyncedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("countByLogin 동작 확인")
    void countByLogin() {
        repository.save(UserAccount.create(701L, "devOne", "a@test.com", null, "A"));
        repository.save(UserAccount.create(702L, "devTwo", "b@test.com", null, "B"));
        repository.save(UserAccount.create(703L, "devThree", "c@test.com", null, "C"));

        long count = repository.countByLogin("dev");
        assertThat(count).isEqualTo(3);
    }
}