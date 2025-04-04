package com.coop.presentation.comment.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record CommentPageResponse<T>(
        List<T> comments,
        int currentPage,
        int totalPages,
        boolean hasNext) {

    public static <T> CommentPageResponse<T> from(Page<T> pageResponse) {
        return CommentPageResponse.<T>builder()
                .comments(pageResponse.getContent())
                .currentPage(pageResponse.getNumber())
                .totalPages(pageResponse.getTotalPages())
                .hasNext(pageResponse.hasNext())
                .build();
    }
}
