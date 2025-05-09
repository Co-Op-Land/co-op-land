package com.coop.domain.post.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.entity.PostDocument;
import org.springframework.stereotype.Component;

@Component
public class PostDocumentMapper {
    public PostDocument toDocument(Post post, Member member) {
        return PostDocument.builder()
                .id(post.getId())
                .title(post.getTitle())
                .contentPreview(generatePreview(post.getContent()))
                .author(member != null ? member.getNickname() : null)
                .category(post.getCategory() != null ? post.getCategory().toString() : null)
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    private String generatePreview(String content) {
        int previewLength = 50;
        return content != null && content.length() > previewLength
                ? content.substring(0, previewLength) + "..."
                : content;
    }
}
