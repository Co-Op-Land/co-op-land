package com.coop.global.websocket;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.ErrorResponseHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 웹소켓 요청 시 빈 토큰 값 필터링용 클래스
 */
@Component
@RequiredArgsConstructor
public class WebSocketFilter extends OncePerRequestFilter {

    private final ErrorResponseHandler errorResponseHandler;

    /**
     * /ws 로 시작하는 요청만 필터링.
     * 그 외 요청은 shouldNotFilter=true 로 필터 스킵
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/ws");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = request.getParameter("token");
        if (token == null || token.isBlank()) {
            errorResponseHandler.send(response, ErrorCode.WEBSOCKET_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}