package com.ssafy.commitmood.domain.user.mapper;

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
import org.springframework.test.context.ActiveProfiles;

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
        Optional<UserAccount> found = mapper.findById(user.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getGithubLogin()).isEqualTo("devys");
        assertThat(found.get().getGithubEmail()).isEqualTo("test@test.com");
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
    @DisplayName("updateProfile 업데이트 테스트")
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
        Optional<UserAccount> found = mapper.findById(user.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getGithubEmail()).isEqualTo("new@test.com");
        assertThat(found.get().getGithubAvatarUrl()).isEqualTo("newAvatar");
        assertThat(found.get().getGithubName()).isEqualTo("New Name");
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
        Optional<UserAccount> found = mapper.findById(user.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getLastSyncedAt()).isEqualTo(now);
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

    /* ------------------------ 🔥 추가된 prefix 조회 테스트 ------------------------ */

    @Test
    @DisplayName("Prefix 기반 단건 조회 테스트(findByPrefixOne)")
    void findByPrefixOne() {
        // given
        mapper.insert(UserAccount.create(201L, "han_yeseong", "han@test.com", null, "한예성"));
        mapper.insert(UserAccount.create(202L, "han_dev", "han2@test.com", null, "한누")); // 2개 중 첫번째만 조회

        // when
        UserAccountQueryCondition condition = new UserAccountQueryCondition("han", null, null); // email prefix
        Optional<UserAccount> result = mapper.findByPrefixOne(condition);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getGithubEmail()).startsWith("han");
    }

    @Test
    @DisplayName("Prefix 기반 전체 목록 조회 테스트(findAllByPrefix)")
    void findAllByPrefix() {
        // given
        mapper.insert(UserAccount.create(301L, "yeseong31", "yse@test.com", null, "예성"));
        mapper.insert(UserAccount.create(302L, "yesman", "ys@test.com", null, "예스맨"));
        mapper.insert(UserAccount.create(303L, "nope", "no@test.com", null, "노"));

        // when
        UserAccountQueryCondition condition = new UserAccountQueryCondition(null, "예", null);
        List<UserAccount> result = mapper.findAllByPrefix(condition);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getGithubName()).startsWith("예");
        assertThat(result.get(1).getGithubName()).startsWith("예");
    }
}