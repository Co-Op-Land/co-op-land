package com.coop.presentation.post.dto.response;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import com.coop.presentation.comment.dto.response.CommentsResponse;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostResponse(
        Long id,
        PostCategory category,
        String nickname,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<CommentsResponse> commentList,
        int currentPage,
        int totalPages,
        boolean hasNext
) {
    public static PostResponse from(Post post, Page<CommentsResponse> pageResponse) {
        return PostResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .commentList(pageResponse.getContent())
                .currentPage(pageResponse.getNumber())
                .totalPages(pageResponse.getTotalPages())
                .hasNext(pageResponse.hasNext())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
