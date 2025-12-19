package com.ssafy.commitmood.common.dto.response;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalCount,
        boolean hasNext,
        int totalPages
) {

    public static <T> PageResponse<T> of(
            List<T> content,
            int page,
            int size,
            long totalCount
    ) {

        int totalPages = (int) Math.ceil((double) totalCount / size);
        boolean hasNextPage = totalPages > page;

        return new PageResponse<T>(
                content,
                page,
                size,
                totalCount,
                hasNextPage,
                totalPages
        );
    }
}
