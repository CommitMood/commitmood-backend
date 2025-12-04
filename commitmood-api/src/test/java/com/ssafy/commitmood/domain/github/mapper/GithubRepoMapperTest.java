package com.ssafy.commitmood.domain.github.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.mapper.UserAccountMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

@MybatisTest
@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GithubRepoMapperTest {

    @Autowired
    UserAccountMapper userMapper;

    @Autowired
    GithubRepoMapper repoMapper;

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
        repoMapper.insert(repo);
        return repo;
    }

    @Test
    @DisplayName("INSERT + findById 정상 동작")
    void insertAndFindById() {
        Long uid = insertUser("devys");
        GithubRepo repo = insertRepo(uid, 1L, "commitmood");

        var found = repoMapper.findById(repo.getId()).orElseThrow();

        assertThat(found.getGithubRepoName()).isEqualTo("commitmood");
        assertThat(found.getUserAccountId()).isEqualTo(uid);
    }


    @Test
    @DisplayName("사용자별 repo 조회 findByUserAccountId")
    void findByUserAccountId() {
        Long uid = insertUser("alpha");
        insertRepo(uid, 10L, "a-repo");
        insertRepo(uid, 11L, "b-repo");

        List<GithubRepo> result = repoMapper.findByUserAccountId(uid);

        assertThat(result).hasSize(2);
    }


    @Test
    @DisplayName("keyword 검색 searchByKeyword LIKE")
    void searchByKeyword() {
        Long uid = insertUser("keywordOwner");
        insertRepo(uid, 21L, "commit-api");
        insertRepo(uid, 22L, "ui-system");
        insertRepo(uid, 23L, "commit-ui");

        List<GithubRepo> result = repoMapper.searchByKeyword("commit");

        assertThat(result).hasSize(2);
    }


    @Test
    @DisplayName("repo 정보 수정 updateInfo()")
    void updateRepo() {
        Long uid = insertUser("upd");
        GithubRepo repo = insertRepo(uid, 33L, "old-name");

        repo.updateInfo(
                "new-name",
                "dev",
                "updated",
                "url-new",
                true
        );

        repoMapper.updateRepo(repo);

        var found = repoMapper.findById(repo.getId()).orElseThrow();

        assertThat(found.getGithubRepoName()).isEqualTo("new-name");
        assertThat(found.getDefaultBranch()).isEqualTo("dev");
        assertThat(found.getDescription()).isEqualTo("updated");
        assertThat(found.getGithubRepoUrl()).isEqualTo("url-new");
        assertThat(found.getIsPrivate()).isTrue();
    }


    @Test
    @DisplayName("lastSyncedAt 변경 updateLastSyncedAt()")
    void updateLastSyncedAt() {
        Long uid = insertUser("sync-owner");
        GithubRepo repo = insertRepo(uid, 44L, "sync-test");

        LocalDateTime now = LocalDateTime.now();

        repoMapper.updateLastSyncedAt(repo.getId(), now);

        var found = repoMapper.findById(repo.getId()).orElseThrow();
        assertThat(found.getLastSyncedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Repo 삭제 deleteById()")
    void deleteRepo() {
        Long uid = insertUser("delete-case");
        GithubRepo repo1 = insertRepo(uid, 501L, "repo-one");
        GithubRepo repo2 = insertRepo(uid, 502L, "repo-two");

        List<GithubRepo> before = repoMapper.findByUserAccountId(uid);
        assertThat(before).hasSize(2);

        // when
        repoMapper.deleteById(repo1.getId());

        // then
        assertThat(repoMapper.findById(repo1.getId())).isEmpty();

        List<GithubRepo> after = repoMapper.findByUserAccountId(uid);
        assertThat(after).hasSize(1);
        assertThat(after.getFirst().getGithubRepoId()).isEqualTo(repo2.getGithubRepoId());
    }
}