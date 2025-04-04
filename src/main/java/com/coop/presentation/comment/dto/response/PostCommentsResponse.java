package com.coop.presentation.comment.dto.response;

import com.coop.domain.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PostCommentsResponse(
        Long commentId,
        Long memberId,
        String nickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long parentId,
        List<PostCommentsResponse> replies
) {
    public static PostCommentsResponse of(Comment comment) {
        List<PostCommentsResponse> replies = comment.getChildren().stream()
                .map(PostCommentsResponse::of)
                .toList();

        return PostCommentsResponse.builder()
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .replies(replies)
                .build();
    }
}
