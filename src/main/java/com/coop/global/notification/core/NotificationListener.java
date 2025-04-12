package com.coop.global.notification.core;

import com.coop.global.websocket.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationListener implements MessageListener {

    private final WebSocketSessionManager sessionManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        String body = new String(message.getBody());
        Long memberId = extractMemberIdFromChannel(channel);

        sessionManager.sendNotificationToMember(memberId, body);
    }

    private Long extractMemberIdFromChannel(String channel) {
        return Long.parseLong(channel.replace("noti:member:", ""));
    }
}
