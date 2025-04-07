package com.coop.global.security;

import com.coop.domain.auth.service.BlackListService;
import com.coop.domain.member.entity.Role;
import com.coop.global.exception.FilterExceptionHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtSecurityProperties jwtSecurityProperties;
    private final BlackListService blackListService;
    private final JwtUtil jwtUtil;
    private final FilterExceptionHandler filterExceptionHandler;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        List<String> whiteList = jwtSecurityProperties.secret().whiteList();

        return whiteList.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request.getRequestURI().startsWith("/ws")) {
            processWebSocketAuthentication(request, response, chain);
            return;
        }

        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String token = jwtUtil.substringToken(bearerJwt);

        if (blackListService.isBlackList(token)) {
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "블랙리스트된 토큰입니다.");
            return;
        }

        try {
            Claims claims = jwtUtil.extractClaims(token);
            if (claims == null || claims.getSubject() == null) {
                filterExceptionHandler.sendErrorResponse(response, HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }
            String[] data = claims.getSubject().split(":");
            String userId = String.valueOf(data[0]);
            Role role = data.length > 1 ? Role.valueOf(data[1]) : null;

            User userDetails = new User(userId, "", role != null ? role.getAuthorities() : null);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
            //SecurityContext 에 인증 정보 저장
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT accessToken, 만료된 JWT accessToken 입니다.", e);
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않은 JWT 서명 입니다.", e);
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT accessToken, 지원되지 않는 JWT 토큰 입니다.", e);
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("Invalid JWT accessToken, 유효하지 않은 JWT 토큰 입니다.", e);
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.BAD_REQUEST, "잘못된 JWT 토큰 형식입니다.");
        } catch (Exception e) {
            log.error("예상치 못한 예외 발생", e);
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");
        }
    }

    private void processWebSocketAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            filterExceptionHandler.sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "웹소켓 토큰을 찾을 수 없습니다.");
            return;
        }
        Claims claims = jwtUtil.extractClaims(jwtUtil.substringToken(token));
        String[] data = claims.getSubject().split(":");
        request.setAttribute("memberId", Long.parseLong(String.valueOf(data[0])));
        chain.doFilter(request, response);
    }
}