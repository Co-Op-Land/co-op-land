package com.coop.global.websocket;

import com.coop.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Arrays;
import java.util.Map;

/**
 * 웹소켓 핸드쉐이킹 -> 연결
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            @NonNull Map<String, Object> attributes
    ) {
        try {
            String query = ((ServletServerHttpRequest)request).getServletRequest().getQueryString();
            String token = Arrays.stream(query.split("&"))
                    .filter(p -> p.startsWith("token="))
                    .map(p -> p.substring(6))
                    .findFirst()
                    .orElse(null);

            Claims claims = jwtUtil.extractClaims(token);
            Long memberId = Long.valueOf(claims.getSubject().split(":")[0]);

            attributes.put("memberId", memberId);
            return true;
        } catch (JwtException e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
    }

    @Override
    public void afterHandshake(
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler,
            Exception exception
    ) { }
}
