package com.coop.global.notification.core;

import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.NotificationMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Kafka 에서 Redis Pub/Sub 으로 event 를 발송하는 메서드
     */
    @KafkaListener(topicPattern = "notification\\..*", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        try {
            NotificationMessage message = NotificationMessage.from(event);
            String payload = objectMapper.writeValueAsString(message);
            for (Long toMemberId : event.toMemberIds().getValues()) {
                String redisChannel = "noti:member:" + toMemberId;
                redisTemplate.convertAndSend(redisChannel, payload);
            }
        } catch (Exception e) {
            log.error("Redis Pub/Sub 전송 실패", e);
            //TODO: retry topic 전송도 고려 가능
        }
    }
}
