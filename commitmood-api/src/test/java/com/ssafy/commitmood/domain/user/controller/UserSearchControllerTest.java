package com.ssafy.commitmood.domain.user.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.commitmood.common.dto.response.PageResponse;
import com.ssafy.commitmood.domain.user.dto.response.UserSearchResponse;
import com.ssafy.commitmood.domain.user.service.UserSearchService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UserSearchControllerTest {

    @Mock
    private UserSearchService searchService;

    @InjectMocks
    private UserSearchController controller;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("GET /api/users/search 단순 검색 성공")
    void search_success() throws Exception {
        // given
        String keyword = "dev";

        List<UserSearchResponse> mockResult = List.of(
                new UserSearchResponse(1L, "devys", "avatar1"),
                new UserSearchResponse(2L, "developer", "avatar2")
        );

        given(searchService.search(keyword)).willReturn(mockResult);

        // when & then
        mockMvc.perform(get("/api/users/search")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        then(searchService).should().search(eq(keyword));
    }

    @Test
    @DisplayName("GET /api/users/search/page 페이징 검색 성공")
    void searchPaged_success() throws Exception {
        // given
        String keyword = "dev";
        int page = 1;
        int size = 10;

        List<UserSearchResponse> content = List.of(
                new UserSearchResponse(1L, "devys", "avatar1"),
                new UserSearchResponse(2L, "developer", "avatar2")
        );

        PageResponse<UserSearchResponse> pageResponse =
                PageResponse.of(content, page, size, 2L);

        given(searchService.search(keyword, page, size)).willReturn(pageResponse);

        // when & then
        mockMvc.perform(get("/api/users/search/page")
                        .param("keyword", keyword)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        then(searchService).should().search(eq(keyword), eq(page), eq(size));
    }
}