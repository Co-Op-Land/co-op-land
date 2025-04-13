package com.coop.global.notification.core;

import com.coop.global.notification.values.NotificationEvent;
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

    @KafkaListener(topics = "notification.comment", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            for (Long toMemberId : event.getRecipients().getValues()) {
                String redisChannel = "noti:member:" + toMemberId;
                redisTemplate.convertAndSend(redisChannel, payload);
                log.info("Kafka → Redis Pub/Sub 전송 완료: {}, 채널: {}", event, redisChannel);
            }
        } catch (Exception e) {
            log.error("Redis Pub/Sub 전송 실패", e);
            //TODO: retry topic 전송도 고려 가능
        }
    }
}
