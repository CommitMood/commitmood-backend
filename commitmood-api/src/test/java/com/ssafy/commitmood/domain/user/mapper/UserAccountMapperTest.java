package com.ssafy.commitmood.domain.user.mapper;

import com.ssafy.commitmood.domain.user.entity.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountMapperTest {

    @Autowired
    UserAccountMapper mapper;

    @Test
    @DisplayName("INSERT + SELECT 테스트")
    void insertAndFind() {
        // given
        UserAccount user = UserAccount.create(
                10L,
                "devys",
                "test@test.com",
                "avatar",
                "Devys"
        );

        // when
        mapper.insert(user);

        // then
        UserAccount found = mapper.findById(user.getId()).orElseThrow();
        assertThat(found.getGithubLogin()).isEqualTo("devys");
        assertThat(found.getGithubEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("LIKE 검색 테스트")
    void searchByLoginLike() {
        // given
        UserAccount user = UserAccount.create(
                11L,
                "helloDev",
                "hello@test.com",
                "avatar",
                "Hello Dev"
        );
        mapper.insert(user);

        // when
        List<UserAccount> result = mapper.searchByLoginLike("hello");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getGithubLogin()).isEqualTo("helloDev");
    }

    @Test
    @DisplayName("updateProfile 테스트")
    void updateProfile() {
        // given
        UserAccount user = UserAccount.create(
                12L,
                "updateUser",
                "old@test.com",
                "oldAvatar",
                "Old Name"
        );
        mapper.insert(user);

        // when
        user.updateProfile("new@test.com", "newAvatar", "New Name");
        mapper.update(user);

        // then
        var found = mapper.findById(user.getId()).orElseThrow();
        assertThat(found.getGithubEmail()).isEqualTo("new@test.com");
        assertThat(found.getGithubAvatarUrl()).isEqualTo("newAvatar");
        assertThat(found.getGithubName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("lastSyncedAt 업데이트 테스트")
    void updateLastSyncedAt() {
        // given
        UserAccount user = UserAccount.create(
                13L,
                "syncUser",
                "sync@test.com",
                "avatar",
                "Sync User"
        );
        mapper.insert(user);

        LocalDateTime now = LocalDateTime.now();

        // when
        mapper.updateLastSyncedAt(user.getId(), now);

        // then
        var found = mapper.findById(user.getId()).orElseThrow();
        assertThat(found.getLastSyncedAt()).isEqualTo(now);
    }

    @Test
    void countByLogin_returnsZero_whenNoMatch() {
        long count = mapper.countByLogin("zzz");
        assertThat(count).isEqualTo(0L);
    }

    @Test
    void countByLogin_returnsCorrectCount_whenPartialMatch() {
        // given
        mapper.insert(UserAccount.create(101L, "devys", "a@test.com", null, "A"));
        mapper.insert(UserAccount.create(102L, "devy", "b@test.com", null, "B"));
        mapper.insert(UserAccount.create(103L, "developer", "c@test.com", null, "C"));

        // when
        long count = mapper.countByLogin("dev");

        // then
        assertThat(count).isEqualTo(3L);
    }
}