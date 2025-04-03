package com.coop.domain.auth.service;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.UnAuthorizedException;
import com.coop.global.security.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtSecurityProperties jwtSecurityProperties;

    /**
     * RefreshToken 을 저장소에 삽입하는 메서드
     * @param userId 현재 사용자의 식별자
     * @param refreshToken AuthService 에서 만들어진 현재 사용자의 refreshToken
     */
    public void createRefreshToken(Long userId, String refreshToken) {
        long expiration = jwtSecurityProperties.token().refreshExpiration();
        String key = getKey(userId);
        redisTemplate.opsForValue().set(key, refreshToken, expiration, TimeUnit.MILLISECONDS);
    }

    /**
     * 저장소에서 RefreshToken 을 가져오는 메서드
     * @param userId 현재 사용자의 식별자
     * @return 토큰이 존재한다면 RefreshToken 반환, 아닐 시 빈 값 반환
     */
    public String getRefreshToken(Long userId) {
        String key = getKey(userId);
        String token = redisTemplate.opsForValue().get(key);
        if (token == null || token.isBlank()) {
            throw new UnAuthorizedException(ErrorCode.TOKEN_UNAUTHORIZED);
        }
        return token;
    }

    /**
     * 사용자의 식별자로 RefreshToken 을 삭제하는 메서드
     * @param userId 현재 사용자의 식별자
     */
    public void deleteRefreshToken(Long userId) {
        String key = getKey(userId);
        redisTemplate.delete(key);
    }

    private String getKey(Long userId) {
        String prefix = jwtSecurityProperties.token().refreshPrefix();
        return prefix + userId;
    }
}


