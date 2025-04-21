package com.coop.global.security;

import com.coop.domain.member.entity.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtSecurityProperties securityProperties;

    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    /**
     * 어플리케이션 초기화 시 JWT secret key 를 초기화하는 메서드
     */
    @PostConstruct
    public void init() {
        String secretKey = securityProperties.secret().key();
        if (!StringUtils.hasText(secretKey)) {
            log.error("JWT secret key 가 비어있습니다.");
            throw new IllegalArgumentException("JWT secret key 가 비어있습니다.");
        }
        try {
            byte[] bytes = Base64.getDecoder().decode(secretKey);
            key = Keys.hmacShaKeyFor(bytes);
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode JWT secret key: {}", e.getMessage());
            throw new IllegalArgumentException("JWT secret key 가 올바르지 않습니다.");
        }
    }

    /**
     * JWT AccessToken 생성(id + role)
     */
    public String createToken(Long memberId, Role role) {
        String payload = memberId + ":" + role;
        return generateJwt(payload,
                securityProperties.token().prefix(),
                securityProperties.token().expiration());
    }

    /**
     * Jwt RefreshToken 생성(id)
     */
    public String createRefreshToken(Long memberId) {
        String payload = String.valueOf(memberId);
        return generateJwt(payload,
                securityProperties.token().refreshPrefix(),
                securityProperties.token().refreshExpiration());
    }

    /**
     * Jwt accessToken 문자열에서 prefix 를 제거하여 순수 토큰 반환
     */
    public String substringToken(String token) {
        String prefix = securityProperties.token().prefix();
        String refreshPrefix = securityProperties.token().refreshPrefix();
        if (StringUtils.hasText(token) && token.startsWith(prefix)) {
            return token.substring(prefix.length()).trim();
        }
        if (StringUtils.hasText(token) && token.startsWith(refreshPrefix)) {
            return token.substring(refreshPrefix.length()).trim();
        }
        throw new IllegalArgumentException("Token 을 찾을 수 없습니다.");
    }

    /**
     * JWT 를 파싱하여 Claims(토큰의 본문) 추출
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 Prefix 제거
     */
    public String removePrefix(String token) {
        return token.substring(securityProperties.token().prefix().length()).trim();
    }

    /**
     * JWT 를 생성하는 내부 메서드
     */
    private String generateJwt(String payload, String prefix, long expiration) {
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(payload)
                .setExpiration(new Date(now.getTime() + expiration))
                .setIssuedAt(now)
                .signWith(key, signatureAlgorithm);

        return prefix + jwtBuilder.compact();
    }
}
