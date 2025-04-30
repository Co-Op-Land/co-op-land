package com.coop.presentation.post.dto.response;

import com.coop.domain.post.entity.PostDocument;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostDocsResponse(
        Long id,
        String title,
        String previewContent,
        String author,
        String category,
        LocalDateTime updatedAt
) {
    public static PostDocsResponse of(PostDocument postDocument) {
        return PostDocsResponse.builder()
                .id(postDocument.getId())
                .title(postDocument.getTitle())
                .previewContent(postDocument.getContentPreview())
                .author(postDocument.getAuthor())
                .category(postDocument.getCategory())
                .updatedAt(postDocument.getUpdatedAt())
                .build();
    }
}

