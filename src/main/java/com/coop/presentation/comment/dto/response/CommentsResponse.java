package com.coop.presentation.comment.dto.response;

import com.coop.domain.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CommentsResponse(
        Long commentId,
        Long memberId,
        String nickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long parentId,
        List<CommentsResponse> replies
) {
    public static CommentsResponse of(Comment comment) {
        List<CommentsResponse> replies = comment.getChildren().stream()
                .map(CommentsResponse::of)
                .toList();

        return CommentsResponse.builder()
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
