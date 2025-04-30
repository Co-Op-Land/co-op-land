package com.coop.domain.post.event;

import com.coop.domain.post.service.PostEsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostCreatedEventListener {

    private final PostEsService postEsService;

    @Async
    @EventListener
    public void handlePostCreateEvent(PostCreatedEvent event) {
        try {
            postEsService.generatePostDocument(event.getPost(), event.getMember());
        } catch (Exception e) {
            log.error("엘라스틱서치 문서 생성 실패", e);
        }
    }
}
