package com.coop.land.auth;

import com.coop.domain.auth.service.BlackListService;
import com.coop.global.security.JwtSecurityProperties;
import com.coop.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AddToBlackListTest {

    @Test
    void addToBlackList_정상등록() {
        String token = "faketoken";
        long ttl = 30_000;
        long now = System.currentTimeMillis();
        String expectedKey = "black:" + token;
        Date expiration = new Date(now + ttl);

        TestContext context = createTestContext(token, expiration);

        context.blackListService.addToBlackList(token);

        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);

        verify(context.valueOps).set(eq(expectedKey), eq("blackList"), timeoutCaptor.capture(), eq(TimeUnit.MILLISECONDS));
        Long actualTimeout = timeoutCaptor.getValue();
        assertTrue(actualTimeout <= ttl + 1500);

    }

    private TestContext createTestContext(String token, Date expiration) {
        RedisTemplate<String, String> redisTemplate = mock(RedisTemplate.class);
        JwtSecurityProperties jwtSecurityProperties = mock(JwtSecurityProperties.class);
        JwtSecurityProperties.Token tokenProps = mock(JwtSecurityProperties.Token.class);
        JwtUtil jwtUtil = mock(JwtUtil.class);
        Claims claims = mock(Claims.class);
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);

        when(jwtSecurityProperties.token()).thenReturn(tokenProps);
        when(tokenProps.blackListPrefix()).thenReturn("black:");
        when(jwtUtil.extractClaims(token)).thenReturn(claims);
        when(claims.getExpiration()).thenReturn(expiration);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        BlackListService blackListService = new BlackListService(redisTemplate, jwtSecurityProperties, jwtUtil);

        return new TestContext(blackListService, valueOps);
    }

    private record TestContext(BlackListService blackListService, ValueOperations<String, String> valueOps) {}
}
