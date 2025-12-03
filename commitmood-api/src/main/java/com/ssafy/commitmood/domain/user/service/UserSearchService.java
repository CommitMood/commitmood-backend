package com.ssafy.commitmood.domain.user.service;

import com.ssafy.commitmood.domain.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.user.dto.response.UserSearchResponse;
import com.ssafy.commitmood.domain.user.entity.UserAccount;
import com.ssafy.commitmood.domain.user.repository.UserAccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserAccountRepository repository;

    public List<UserSearchResponse> search(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        return repository.searchByLogin(keyword).stream()
                .map(UserSearchResponse::of)
                .toList();
    }

    public PageResponse<UserSearchResponse> search(String keyword, int page, int size) {

        int offset = (page - 1) * size;
        List<UserAccount> results = repository.searchByLoginPaged(keyword, size, offset);
        long totalCount = repository.countByLogin(keyword);

        List<UserSearchResponse> responses = results.stream()
                .map(UserSearchResponse::of)
                .toList();

        return PageResponse.of(responses, page, size, totalCount);
    }
}
