package com.coop.domain.post.service;

import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadLetterQueueService {

    private final PostSearchRepository postSearchRepository;

    @RabbitListener(queues = "post.dlq")
    public void processFailedMessages(PostDocument failedEvent) {
        log.error("Failed to process message: {}", failedEvent);

        try {
            // 다른 방식으로 색인 시도? 재시도? 고민..
            postSearchRepository.save(failedEvent);
        } catch (Exception e) {
            log.error("Secondary indexing attempt failed", e);
        }
    }
}
