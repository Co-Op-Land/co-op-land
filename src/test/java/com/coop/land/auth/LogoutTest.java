package com.coop.land.auth;

import com.coop.domain.auth.service.AuthService;
import com.coop.domain.auth.service.BlackListService;
import com.coop.domain.auth.service.RefreshTokenService;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.UnAuthorizedException;
import com.coop.global.security.JwtSecurityProperties;
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
    private JwtSecurityProperties jwtSecurityProperties;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private BlackListService blackListService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testLogout_성공케이스() {
        String prefix = "Bearer ";
        String fullToken = "Bearer faketoken";
        String rawToken = "faketoken";
        Long userId = 1L;

        JwtSecurityProperties.Token mockToken = mock(JwtSecurityProperties.Token.class);
        setUpJwtProperties(mockToken);

        Claims claims = mock(Claims.class);
        when(jwtUtil.substringToken(fullToken)).thenReturn(rawToken);
        when(jwtUtil.extractClaims(rawToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn(userId + ":USER");
        when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 10000));

        assertDoesNotThrow(() -> authService.logout(fullToken));

        verify(blackListService).addToBlackList(rawToken);
        verify(refreshTokenService).deleteRefreshToken(userId);
    }

    @Test
    void testLogout_토큰없을때() {
        String emptyHeader = "";
        JwtSecurityProperties.Token mockToken = mock(JwtSecurityProperties.Token.class);
        setUpJwtProperties(mockToken);

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> authService.logout(emptyHeader));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }

    private void setUpJwtProperties(JwtSecurityProperties.Token mockToken) {
        when(jwtSecurityProperties.token()).thenReturn(mockToken);
        when(mockToken.prefix()).thenReturn("Bearer");
    }
}
