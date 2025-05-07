package com.coop.domain.post.service;

import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostIndexingService {

    private final PostSearchRepository postSearchRepository;

    @RabbitListener(queues = "post.queue", concurrency = "5-10")//concurrency를 조절하여 병렬처리 할 개수 조정 가능
    public void indexPost(PostDocument event) {
        postSearchRepository.save(event);
    }
}
