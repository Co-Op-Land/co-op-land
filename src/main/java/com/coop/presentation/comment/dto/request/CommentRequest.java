package com.coop.presentation.comment.dto.request;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import lombok.Builder;

@Builder
public record CommentRequest(String content, Long parentId) {
    public Comment of(Member member, Post post, Comment parent) {
        return Comment.builder()
                .content(content)
                .member(member)
                .post(post)
                .parent(parent)
                .build();
    }
}
