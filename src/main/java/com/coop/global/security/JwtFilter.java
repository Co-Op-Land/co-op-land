package com.coop.global.security;

import com.coop.domain.auth.service.BlackListService;
import com.coop.domain.member.entity.Role;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.ErrorResponseHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtSecurityProperties jwtSecurityProperties;
    private final BlackListService blackListService;
    private final JwtUtil jwtUtil;
    private final ErrorResponseHandler errorResponseHandler;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return jwtSecurityProperties.secret().whiteList().stream()
                .anyMatch(whitelist -> antPathMatcher.match(whitelist, uri));
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/ws")) {
            processWebSocketAuthentication(request, response, chain);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt != null && bearerJwt.startsWith(jwtSecurityProperties.token().prefix())) {
            String token = jwtUtil.substringToken(bearerJwt);
            if (blackListService.isBlackList(token)) {
                errorResponseHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
                return;
            }
            try {
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(token);
                }
            } catch (ExpiredJwtException e) {
                log.error("만료된 JWT 토큰입니다.");
                errorResponseHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
                return;
            } catch (SecurityException | MalformedJwtException e) {
                log.error("유효하지 않은 JWT 서명입니다.");
                errorResponseHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
                return;
            } catch (UnsupportedJwtException e) {
                log.error("지원되지 않는 JWT 토큰입니다.");
                errorResponseHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
                return;
            } catch (IllegalArgumentException e) {
                log.error("잘못된 JWT 토큰 형식입니다.");
                errorResponseHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                log.error("예기치 못한 오류.");
                errorResponseHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * Context 에 인증정보 저장
     */
    private void setAuthentication(String token) {
        Claims claims = jwtUtil.extractClaims(token);
        String[] data = claims.getSubject().split(":");
        Long memberId = Long.valueOf(data[0]);
        Role userRole = Role.of(data[1]);

        AuthUser authUser = AuthUser.of(memberId, userRole);
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser, token);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /**
     * WebSocket 인증
     */
    private void processWebSocketAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            filterExceptionHandler.send(response, ErrorCode.TOKEN_UNAUTHORIZED);
            return;
        }
        Claims claims = jwtUtil.extractClaims(jwtUtil.substringToken(token));
        String[] data = claims.getSubject().split(":");
        request.setAttribute("memberId", Long.parseLong(String.valueOf(data[0])));
        chain.doFilter(request, response);
    }
}