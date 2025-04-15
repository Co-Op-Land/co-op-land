package com.coop.global.notification.core;

import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.NotificationMessage;
import com.coop.global.websocket.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationConsumer {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final WebSocketSessionManager sessionManager;

    @KafkaListener(topicPattern = "notification\\..*", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        try {
            NotificationMessage message = NotificationMessage.from(event);
            String payload = objectMapper.writeValueAsString(message);

            //현재 접속 중인 유저만 필터링
            List<Long> connectedUserIds = sessionManager.getConnectedUserIdList();
            List<Long> toSendIds = event.toMemberIds().getValues().stream()
                    .filter(connectedUserIds::contains)
                    .toList();

            for (Long toMemberId : toSendIds) {
                String redisChannel = "noti:member:" + toMemberId;
                redisTemplate.convertAndSend(redisChannel, payload);
            }

        } catch (Exception e) {
            log.error("Redis Pub/Sub 전송 실패", e);
        }
    }
}

