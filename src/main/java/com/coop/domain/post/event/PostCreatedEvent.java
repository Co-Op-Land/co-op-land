package com.coop.domain.post.event;

import com.coop.domain.member.entity.Member;
import com.coop.domain.post.entity.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostCreatedEvent {
    private final Post post;
    private final Member member;
}
