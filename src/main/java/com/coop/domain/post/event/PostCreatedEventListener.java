package com.coop.domain.post.event;

import com.coop.domain.post.entity.PostDocument;
import com.coop.domain.post.service.PostDocumentMapper;
import com.coop.domain.post.service.PostEsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("async")
@RequiredArgsConstructor
public class PostCreatedEventListener {

    private final PostEsService postEsService;

    @Async
    @EventListener
    public void handlePostCreateEvent(PostDocument event) {
        try {
            postEsService.saveDocument(event);
        } catch (Exception e) {
            log.error("엘라스틱서치 문서 생성 실패", e);
        }
    }
}
