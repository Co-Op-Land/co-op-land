package com.coop.presentation.post.dto.response;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostsResponse(
        Long id,
        PostCategory category,
        String nickname,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostsResponse of(Post post) {
        return PostsResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
