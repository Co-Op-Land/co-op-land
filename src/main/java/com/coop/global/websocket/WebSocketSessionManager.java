package com.coop.global.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketSessionManager {

    private final ConcurrentHashMap<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addSession(Long memberId, WebSocketSession session) {
        sessions.put(memberId, session);
    }

    public void removeSession(Long memberId) {
        sessions.remove(memberId);
    }

    public WebSocketSession getSession(Long memberId) {
        return sessions.get(memberId);
    }

    public void sendNotificationToMember(Long memberId, String message) {
        sendNotification(Collections.singletonList(memberId), message);
    }

    public void sendNotificationToMember(List<Long> memberIds, String message) {
        sendNotification(memberIds, message);
    }

    private void sendNotification(List<Long> memberIds, String message) {
        for (Long memberId : memberIds) {
            WebSocketSession session = getSession(memberId);
            if (session != null) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(message));
                    } else {
                        log.warn("유저 {}의 웹소켓 세션을 삭제합니다.", memberId);
                        removeSession(memberId);
                    }
                } catch (IOException e) {
                    log.error("유저 {}에게 웹소켓 메시지 전송을 실패하였습니다: {}", memberId, e.getMessage());
                    removeSession(memberId);
                }
            } else {
                log.warn("유저 {}의 웹소켓 연결 에러", memberId);
            }
        }
    }
}
