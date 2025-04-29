package com.coop.global.websocket;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class WebSocketSessionManager {

    private final ConcurrentHashMap<Long, CopyOnWriteArraySet<String>> memberSessions = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> sessionToMember = new ConcurrentHashMap<>();

    public void addSession(Long memberId, String sessionId) {
        memberSessions
                .computeIfAbsent(memberId, k -> new CopyOnWriteArraySet<>())
                .add(sessionId);
        sessionToMember.put(sessionId, memberId);
    }

    public void removeSession(String sessionId) {
        Long memberId = sessionToMember.remove(sessionId);
        if (memberId == null) return;

        CopyOnWriteArraySet<String> set = memberSessions.get(memberId);
        if (set != null) {
            set.remove(sessionId);
            if (set.isEmpty()) memberSessions.remove(memberId);
        }
    }

    public List<Long> getConnectedUserIdList() {
        return new ArrayList<>(memberSessions.keySet());
    }
}

