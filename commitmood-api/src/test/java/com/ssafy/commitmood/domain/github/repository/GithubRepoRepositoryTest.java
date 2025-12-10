package com.ssafy.commitmood.domain.github.repository;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(GithubRepoRepositoryImpl.class)
class GithubRepoRepositoryTest {

    @Autowired
    GithubRepoRepository repoRepository;

    @Autowired
    UserAccountMapper userMapper;

    private Long insertUser(String login) {
        UserAccount user = UserAccount.create(
                System.nanoTime(),
                login,
                login + "@mail.com",
                "avatar",
                login + "Name"
        );
        userMapper.insert(user);
        return user.getId();
    }

    private GithubRepo insertRepo(Long userId, Long repoId, String name) {
        GithubRepo repo = GithubRepo.create(
                userId,
                repoId,
                name,
                "devys/" + name,
                "main",
                "desc",
                "https://github.com/devys/" + name,
                false
        );
        return repoRepository.save(repo);
    }

    @Test
    @DisplayName("save + findById 성공")
    void saveAndFind() {
        Long uid = insertUser("repoUser");
        GithubRepo repo = insertRepo(uid, 100L, "commitmood");

        Optional<GithubRepo> found = repoRepository.findById(repo.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getGithubRepoName()).isEqualTo("commitmood");
    }

    @Test
    @DisplayName("findByUserAccountId 조회")
    void findByUserAccountId() {
        Long uid = insertUser("multi");
        insertRepo(uid, 11L, "one");
        insertRepo(uid, 12L, "two");

        List<GithubRepo> list = repoRepository.findByUserAccountId(uid);

        assertThat(list).hasSize(2);
    }

    @Test
    @DisplayName("search(keyword) LIKE 검색")
    void searchKeyword() {
        Long uid = insertUser("searchUser");
        insertRepo(uid, 21L, "commit-api");
        insertRepo(uid, 22L, "ui-system");
        insertRepo(uid, 23L, "commit-core");

        List<GithubRepo> result = repoRepository.search("commit");

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("updateInfo() 정상 반영")
    void updateInfo() {
        Long uid = insertUser("updateTest");
        GithubRepo repo = insertRepo(uid, 33L, "old");

        repo.updateInfo(
                "new-name",
                "dev",
                "updated",
                "url-new",
                true
        );
        repoRepository.updateInfo(repo);

        GithubRepo found = repoRepository.findById(repo.getId()).orElseThrow();
        assertThat(found.getGithubRepoName()).isEqualTo("new-name");
        assertThat(found.getIsPrivate()).isTrue();
    }

    @Test
    @DisplayName("updateLastSyncedAt() 성공")
    void updateLastSyncedAt() {
        Long uid = insertUser("timeUser");
        GithubRepo repo = insertRepo(uid, 55L, "time-repo");

        LocalDateTime now = LocalDateTime.now();
        repoRepository.updateLastSyncedAt(repo.getId(), now);

        GithubRepo found = repoRepository.findById(repo.getId()).orElseThrow();
        assertThat(found.getLastSyncedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("deleteById() 삭제 검증")
    void deleteRepo() {
        Long uid = insertUser("del");
        GithubRepo repo1 = insertRepo(uid, 501L, "repo-one");
        insertRepo(uid, 502L, "repo-two");

        repoRepository.deleteById(repo1.getId());

        assertThat(repoRepository.findById(repo1.getId())).isEmpty();
        assertThat(repoRepository.findByUserAccountId(uid)).hasSize(1);
    }
}