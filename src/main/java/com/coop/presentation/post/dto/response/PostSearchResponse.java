package com.coop.presentation.post.dto.response;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostSearchResponse(
        Long id,
        PostCategory category,
        String nickname,
        String title,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostSearchResponse from(Post post) {
        return PostSearchResponse.builder()
                .id(post.getId())
                .category(post.getCategory())
                .nickname(post.getMember().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }
}
