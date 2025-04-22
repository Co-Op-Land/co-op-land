package com.coop.global.notification.core;

import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.NotificationMessage;
import com.coop.global.websocket.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationConsumer {

    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    @KafkaListener(topicPattern = "notification\\..*", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        try {
            NotificationMessage message = NotificationMessage.from(event);
            String payload = objectMapper.writeValueAsString(message);

            List<Long> toSendIds = event.toMemberIds()
                    .filterConnected(sessionManager.getConnectedUserIdList())
                    .getValues();

            for (Long toMemberId : toSendIds) {
                String destination = "/topic/notifications/" + toMemberId;
                messagingTemplate.convertAndSend(destination, payload);
            }

        } catch (Exception e) {
            log.error("STOMP WebSocket 전송 실패", e);
        }
    }
}
