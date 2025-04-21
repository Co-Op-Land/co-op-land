package com.coop.global.websocket;

import com.coop.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;

@Slf4j
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;

    public WebSocketHandshakeInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) {
        String query = ((ServletServerHttpRequest)request).getServletRequest().getQueryString();
        String token = Arrays.stream(query.split("&"))
                .filter(p -> p.startsWith("token="))
                .map(p -> p.substring(6))
                .findFirst()
                .orElse(null);
        if (token == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        Claims claims = jwtUtil.extractClaims(token);
        Long memberId = Long.valueOf(claims.getSubject().split(":")[0]);

        attributes.put("memberId", memberId);
        return true;
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception
    ) { }
}
