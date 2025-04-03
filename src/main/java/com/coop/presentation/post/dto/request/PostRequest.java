package com.coop.presentation.post.dto.request;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.enums.PostCategory;
import lombok.Builder;

@Builder
public record PostRequest(String title, String content, String category) {
    public Post of(Member member) {
        return Post.builder()
                .member(member)
                .title(title)
                .content(content)
                .category(PostCategory.of(category))
                .build();
    }
}
