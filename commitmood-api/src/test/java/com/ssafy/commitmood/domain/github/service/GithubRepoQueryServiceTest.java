package com.ssafy.commitmood.domain.github.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.commit.entity.CommitLog;
import com.ssafy.commitmood.domain.commit.repository.CommitLogMapper;
import com.ssafy.commitmood.domain.github.dto.response.GithubCommitResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoListResponse;
import com.ssafy.commitmood.domain.github.dto.response.GithubRepoResponse;
import com.ssafy.commitmood.domain.github.entity.GithubRepo;
import com.ssafy.commitmood.domain.github.repository.GithubRepoRepository;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GithubRepoQueryServiceTest {

    @Autowired
    GithubRepoQueryService repoQueryService;

    @Autowired
    GithubRepoRepository repoRepository;

    @Autowired
    UserAccountRepository userRepository;

    @Autowired
    CommitLogMapper commitLogMapper;

    private Long createUser(String login) {
        UserAccount user = UserAccount.create(
                System.nanoTime(),          // githubUserId (unique)
                login,
                login + "@mail.com",
                "avatar",
                login + "Name"
        );
        userRepository.save(user);
        return user.getId();
    }

    private GithubRepo createRepo(Long userId, long repoSeq, String name) {
        GithubRepo repo = GithubRepo.create(
                userId,
                repoSeq,
                name,
                "devys/" + name,
                "main",
                "desc",
                "https://github.com/devys/" + name,
                false
        );
        repoRepository.save(repo);
        return repo;
    }

    @Test
    @DisplayName("특정 사용자의 Repo 목록을 조회할 수 있다")
    void getUserRepos() {
        Long userId = createUser("owner1");
        createRepo(userId, 1L, "commitmood-api");
        createRepo(userId, 2L, "commitmood-batch");

        GithubRepoListResponse result = repoQueryService.getUserRepos(userId);

        assertThat(result.repoList()).hasSize(2);
        assertThat(result.repoList())
                .extracting(GithubRepoResponse::name)
                .containsExactlyInAnyOrder("commitmood-api", "commitmood-batch");
    }

    @Test
    @DisplayName("Repo 단건 조회가 정상 동작한다")
    void getRepo() {
        Long userId = createUser("single");
        GithubRepo saved = createRepo(userId, 10L, "single-repo");

        GithubRepoResponse response = repoQueryService.getRepo(saved.getId());

        assertThat(response.id()).isEqualTo(saved.getId());
        assertThat(response.userAccountId()).isEqualTo(userId);
        assertThat(response.name()).isEqualTo("single-repo");
    }

    @Test
    @DisplayName("존재하지 않는 Repo 조회 시 예외를 던진다")
    void getRepo_notFound() {
        assertThatThrownBy(() -> repoQueryService.getRepo(9999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("키워드 기반 Repo 검색에 페이징을 적용할 수 있다")
    void searchPaged() {
        Long userId = createUser("searchUser");

        // keyword "commit" 이 포함된 repo 3개
        createRepo(userId, 21L, "commit-api");
        createRepo(userId, 22L, "commit-ui");
        createRepo(userId, 23L, "commit-core");

        // 매칭되지 않는 repo
        createRepo(userId, 24L, "other-repo");

        int page = 1;   // 1번 페이지
        int size = 2;   // 페이지당 2건

        PageResponse<GithubRepoResponse> result = repoQueryService.searchPaged("commit", page, size);

        // 전체 3건 중 2건만 내려온다
        assertThat(result.content()).hasSize(2);
        assertThat(result.totalCount()).isEqualTo(3L);
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.hasNext()).isTrue();
    }

    @Test
    @DisplayName("마지막 페이지에서는 hasNext가 false가 된다")
    void searchPaged_lastPage() {
        Long userId = createUser("pagingUser");

        createRepo(userId, 31L, "commit-a");
        createRepo(userId, 32L, "commit-b");
        createRepo(userId, 33L, "commit-c");

        int page = 2;
        int size = 2;

        PageResponse<GithubRepoResponse> result = repoQueryService.searchPaged("commit", page, size);

        // 2페이지에는 1건
        assertThat(result.content()).hasSize(1);
        assertThat(result.totalCount()).isEqualTo(3L);
        assertThat(result.totalPages()).isEqualTo(2);
        assertThat(result.page()).isEqualTo(2);
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    @DisplayName("특정 Repo의 Commit 목록을 조회할 수 있다")
    void getCommitsByRepo() {
        // ---------------------------------------------
        // GIVEN: User + Repo + CommitLog 구성
        // ---------------------------------------------

        Long userId = createUser("commitUser");
        GithubRepo repo = createRepo(userId, 1001L, "commitmood-api");

        String sha = "a".repeat(40);

        CommitLog commit = CommitLog.create(
                repo.getId(),
                userId,
                sha,
                LocalDateTime.now(),
                "feat: add new api",
                "https://github.com/devys/commitmood-api/commit/" + sha,
                10L,
                2L,
                12L,
                3L
        );

        commitLogMapper.insert(commit);

        // ---------------------------------------------
        // WHEN: 서비스에서 getCommitsByRepo 호출
        // ---------------------------------------------
        List<GithubCommitResponse> result = repoQueryService.getCommitsByRepo(repo.getId());

        // ---------------------------------------------
        // THEN: CommitLog → GithubCommitResponse 매핑 확인
        // ---------------------------------------------
        assertThat(result).hasSize(1);

        GithubCommitResponse first = result.get(0);

        assertThat(first.id()).isNotNull();
        assertThat(first.repoId()).isEqualTo(repo.getId());
        assertThat(first.userAccountId()).isEqualTo(userId);
        assertThat(first.sha()).isEqualTo(sha);
        assertThat(first.message()).isEqualTo("feat: add new api");
        assertThat(first.totalChanges()).isEqualTo(12);
    }
}