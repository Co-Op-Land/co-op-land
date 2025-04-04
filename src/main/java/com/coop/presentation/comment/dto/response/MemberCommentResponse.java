package com.coop.presentation.comment.dto.response;

import com.coop.domain.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record MemberCommentResponse(
        Long commentId,
        Long memberId,
        String nickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static MemberCommentResponse of(Comment comment) {
        return MemberCommentResponse.builder()
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
