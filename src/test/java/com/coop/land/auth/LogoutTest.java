package com.coop.land.auth;

import com.coop.domain.auth.service.AuthService;
import com.coop.domain.auth.service.BlackListService;
import com.coop.domain.auth.service.RefreshTokenService;
import com.coop.global.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private BlackListService blackListService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testLogout_성공케이스() {
        String rawToken = "faketoken";
        Long userId = 1L;

        Claims claims = mock(Claims.class);
        when(jwtUtil.extractClaims(rawToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(userId + ":USER");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000));

        assertDoesNotThrow(() -> authService.logout(rawToken));

        verify(blackListService).addToBlackList(rawToken);
        verify(refreshTokenService).deleteRefreshToken(userId);
    }

    @Test
    void testLogout_토큰없을때() {
        String nullToken = null;

        when(jwtUtil.extractClaims(isNull())).thenThrow(new IllegalArgumentException("Token 을 찾을 수 없습니다."));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> authService.logout(nullToken)
        );
        assertEquals("Token 을 찾을 수 없습니다.", exception.getMessage());
    }
}
