package com.coop.domain.post.service;

import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.repository.PostSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Profile("rabbit")
@RequiredArgsConstructor
public class PostIndexingService {

    private final PostSearchRepository postSearchRepository;

    @RabbitListener(queues = "post.queue", concurrency = "5-10")
    public void indexPost(PostDocument event) {
        //인덱스싱 처리가 성공/실패되었을 경우 성공/실패 여부를 추적할 수 있도록 로깅 등의 처리가 필요할 듯 하다.
        log.info("메시지 큐 처리: {} ", event);
        postSearchRepository.save(event);
    }

    // 배치 처리 버전
//    @RabbitListener(queues = "post.queue", containerFactory = "batchRabbitListenerContainerFactory")
    public void indexPosts(List<PostDocument> events) {
        if (events.isEmpty()) {
            return;
        }

        try {
            postSearchRepository.saveAll(events);
            log.info("Batch indexed {} posts", events.size());
        } catch (Exception e) {
            log.error("Error batch indexing {} posts", events.size(), e);
            throw e;
        }
    }

    @Bean
    public SimpleRabbitListenerContainerFactory batchRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setBatchListener(true);
        factory.setBatchSize(100);
        factory.setConsumerBatchEnabled(true);
        return factory;
    }
}
