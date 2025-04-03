package com.coop.domain.auth.service;

import com.coop.global.security.JwtSecurityProperties;
import com.coop.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class BlackListService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final JwtUtil jwtUtil;

    /**
     * 사용자의 AccessToken 을 블랙리스트에 저장하는 메서드
     * @param token 현재 사용자의 AccessToken
     */
    public void addToBlackList(String token) {
        String blackPrefix = jwtSecurityProperties.token().blackListPrefix();
        String key = blackPrefix + token;

        Claims claims = jwtUtil.extractClaims(token);
        long expirationMillis = claims.getExpiration().getTime() - System.currentTimeMillis();

        if (expirationMillis > 0) {
            redisTemplate.opsForValue().set(key, "blackList", expirationMillis + 1000, TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 블랙리스트에 등록된 토큰인지 검증하는 메서드
     * @param token 현재 사용자의 AccessToken
     * @return 블랙리스트에 들어있다면 true / 아니면 false
     */
    public boolean isBlackList(String token) {
        String blackPrefix = jwtSecurityProperties.token().blackListPrefix();
        String key = blackPrefix + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
