package com.coop.global.notification.core;

import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.NotificationMessage;
import com.coop.global.websocket.WebSocketSessionManager;
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

    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager sessionManager;

    @KafkaListener(topicPattern = "notification\\..*", groupId = "notification-group")
    public void consume(NotificationEvent event) {
        log.info("event: {}", event);
        NotificationMessage message = NotificationMessage.from(event);

        List<Long> connectedIds = sessionManager.getConnectedUserIdList();
        event.toMemberIds().filterConnected(connectedIds)
                .getValues().forEach(id -> messagingTemplate.convertAndSendToUser(
                        String.valueOf(id), "/queue/notifications", message));
    }
}

