package com.coop.global.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class WebSocketSessionManager {

    private final ConcurrentHashMap<Long, CopyOnWriteArrayList<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    public void addSession(Long memberId, WebSocketSession session) {
        sessions.computeIfAbsent(memberId, k -> new CopyOnWriteArrayList<>()).add(session);
    }

    public void removeSession(Long memberId, WebSocketSession session) {
        List<WebSocketSession> memberSessions = sessions.get(memberId);
        if (memberSessions != null) {
            memberSessions.remove(session);
            if (memberSessions.isEmpty()) {
                sessions.remove(memberId);
            }
        }
    }

    public void sendNotificationToMember(Long memberId, String message) {
        sendNotification(List.of(memberId), message);
    }

    private void sendNotification(List<Long> memberIds, String message) {
        for (Long memberId : memberIds) {
            List<WebSocketSession> memberSessions = sessions.get(memberId);
            if (memberSessions != null) {
                for (WebSocketSession session : memberSessions) {
                    try {
                        if (session.isOpen()) {
                            session.sendMessage(new TextMessage(message));
                        } else {
                            log.warn("세션이 닫혀있습니다. 유저 {}의 세션 제거", memberId);
                            removeSession(memberId, session);
                        }
                    } catch (IOException e) {
                        log.error("유저 {}에게 메시지 전송 실패: {}", memberId, e.getMessage());
                        removeSession(memberId, session);
                    }
                }
            } else {
                log.warn("유저 {}의 웹소켓 세션이 존재하지 않음", memberId);
            }
        }
    }

    public List<Long> getConnectedUserIdList() {
        return new ArrayList<>(sessions.keySet());
    }
}
