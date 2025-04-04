package com.coop.presentation.post.dto.response;

import lombok.Builder;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
public record PostPageResponse(List<PostsResponse> posts, int currentPage, int totalPages, boolean hasNext) {
    public static PostPageResponse from(Page<PostsResponse> pageResponse) {
        return PostPageResponse.builder()
                .posts(pageResponse.getContent())
                .currentPage(pageResponse.getNumber())
                .totalPages(pageResponse.getTotalPages())
                .hasNext(pageResponse.hasNext())
                .build();
    }
}