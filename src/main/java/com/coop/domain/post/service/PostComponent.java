package com.coop.domain.post.service;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostComponent {

    private final PostRepository postRepository;

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_POST));
    }
}
