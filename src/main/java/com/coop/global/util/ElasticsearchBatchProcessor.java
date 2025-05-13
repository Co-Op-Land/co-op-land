package com.coop.global.util;

import com.coop.domain.post.event.PostCreatedEvent;
import com.coop.domain.post.service.PostEsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchBatchProcessor {

    private static final int BATCH_SIZE = 1000;
    private final Queue<PostCreatedEvent> indexingQueue = new ConcurrentLinkedQueue<>();
    private final Queue<PostCreatedEvent> failedQueue = new ConcurrentLinkedQueue<>();
    private final PostEsService postEsService;

    public void addToQueue(PostCreatedEvent event) {
        indexingQueue.add(event);

        if (indexingQueue.size() >= BATCH_SIZE) {
            processBatch();
        }
    }

    @Async
    protected synchronized void processBatch() {
        List<PostCreatedEvent> events = new ArrayList<>(BATCH_SIZE);
        int processedCount = 0;

        while (!indexingQueue.isEmpty() && processedCount < BATCH_SIZE) {
            PostCreatedEvent event = indexingQueue.poll();
            if (event != null) {
                events.add(event);
                processedCount++;
            }
        }

        if (!events.isEmpty()) {
            try {
                postEsService.saveAllFromEvents(events);
                log.info("배치 처리 완료: {}개 포스트 인덱싱", events.size());
            } catch (Exception e) {
                log.error("배치 인덱싱 실패:", e);
                failedQueue.addAll(events);
            }
        }
    }

    @Scheduled(fixedDelay = 1800000) // 30m
    public void scheduledBatchProcessing() {
        if (!indexingQueue.isEmpty()) {
            processBatch();
        }
    }

    @Scheduled(fixedDelay = 1800000) // 30m
    public void retryFailedBatch() {
        if (!failedQueue.isEmpty()) {
            List<PostCreatedEvent> retryEvents = new ArrayList<>();
            while (!failedQueue.isEmpty() && retryEvents.size() < BATCH_SIZE) {
                PostCreatedEvent event = failedQueue.poll();
                if (event != null) retryEvents.add(event);
            }

            try {
                postEsService.saveAllFromEvents(retryEvents);
                log.info("실패한 배치 재시도 성공: {}개", retryEvents.size());
            } catch (Exception e) {
                log.error("실패한 배치 재시도 실패", e);
                failedQueue.addAll(retryEvents);
            }
        }
    }
}
