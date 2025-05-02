package com.coop.domain.post.event;

import com.coop.global.util.ElasticsearchBatchProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostCreatedEventListener {

    private final ElasticsearchBatchProcessor elasticsearchBatchProcessor;

    @Async
    @EventListener
    public void handlePostCreateEvent(PostCreatedEvent event) {
        try {
            elasticsearchBatchProcessor.addToQueue(event);
        } catch (Exception e) {
            log.error("엘라스틱서치 문서 큐 추가 실패", e);
        }
    }
}
