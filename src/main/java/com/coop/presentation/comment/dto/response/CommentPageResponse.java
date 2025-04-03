package com.coop.presentation.comment.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record CommentPageResponse(List<CommentsResponse> comments, int currentPage, int totalPages, boolean hasNext) {
    public static CommentPageResponse from(Page<CommentsResponse> pageResponse) {
        return CommentPageResponse.builder()
                .comments(pageResponse.getContent())
                .currentPage(pageResponse.getNumber())
                .totalPages(pageResponse.getTotalPages())
                .hasNext(pageResponse.hasNext())
                .build();
    }
}
