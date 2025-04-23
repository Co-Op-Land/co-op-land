package com.coop.land.auth;

import com.coop.domain.auth.service.BlackListService;
import com.coop.global.security.JwtSecurityProperties;
import com.coop.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddToBlackListTest {

    @Mock private RedisTemplate<String, String> redisTemplate;
    @Mock private JwtSecurityProperties jwtSecurityProperties;
    @Mock private JwtSecurityProperties.Token tokenProps;
    @Mock private JwtUtil jwtUtil;
    @Mock private Claims claims;
    @Mock private ValueOperations<String, String> valueOps;

    @InjectMocks
    private BlackListService blackListService;

    @BeforeEach
    void setUp() {
        when(jwtSecurityProperties.token()).thenReturn(tokenProps);
        when(tokenProps.blackListPrefix()).thenReturn("black:");
        when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Test
    void addToBlackList_정상등록() {
        String token = "faketoken";
        long ttl = 30_000;
        long now = System.currentTimeMillis();
        Date expiration = new Date(now + ttl);

        when(jwtUtil.extractClaims(token)).thenReturn(claims);
        when(claims.getExpiration()).thenReturn(expiration);

        blackListService.addToBlackList(token);

        ArgumentCaptor<Long> timeoutCaptor = ArgumentCaptor.forClass(Long.class);
        verify(valueOps).set(eq("black:" + token), eq("blackList"), timeoutCaptor.capture(), eq(TimeUnit.MILLISECONDS));
        Long actualTimeout = timeoutCaptor.getValue();
        assertTrue(actualTimeout <= ttl + 1500);
    }
}
