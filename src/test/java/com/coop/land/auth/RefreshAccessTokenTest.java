package com.coop.land.auth;

import com.coop.domain.auth.service.AuthService;
import com.coop.domain.auth.service.RefreshTokenService;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.UnAuthorizedException;
import com.coop.global.security.JwtUtil;
import com.coop.presentation.auth.dto.response.RefreshAccessTokenResponse;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshAccessTokenTest {

    @Mock
    private MemberComponent memberComponent;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRefreshAccessToken_액세스토큰_재발급_성공했을때() {
        String refreshToken = "validRefreshToken";
        Claims claims = mock(Claims.class);
        Member member = mock(Member.class);

        when(jwtUtil.extractClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(refreshTokenService.getRefreshToken(1L)).thenReturn("validRefreshToken");
        when(jwtUtil.substringToken(refreshToken)).thenReturn("validRefreshToken");
        when(jwtUtil.substringToken("validRefreshToken")).thenReturn("validRefreshToken");
        when(jwtUtil.createRefreshToken(1L)).thenReturn("newRefreshToken");
        when(memberComponent.findById(1L)).thenReturn(member);
        when(jwtUtil.createToken(1L, member.getRole())).thenReturn("rawNewAccessToken");
        when(jwtUtil.removePrefix("rawNewAccessToken")).thenReturn("newAccessToken");

        RefreshAccessTokenResponse response = authService.refreshAccessToken(refreshToken);

        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
    }

    @Test
    void testRefreshAccessToken_존재하지_않는_토큰() {
        String rawToken = "Bearer invalid";
        String strippedToken = "invalid";

        when(jwtUtil.substringToken(rawToken)).thenReturn(strippedToken);
        when(jwtUtil.extractClaims(strippedToken))
                .thenThrow(new UnAuthorizedException(ErrorCode.TOKEN_UNAUTHORIZED));

        UnAuthorizedException exception = assertThrows(
                UnAuthorizedException.class,
                () -> authService.refreshAccessToken(rawToken)
        );
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }

    @Test
    void testRefreshAccessToken_리프레시토큰_불일치() {
        String rawToken = "Bearer validRefreshToken";
        String strippedToken = "validRefreshToken";
        String storedToken = "differentStoredToken";
        Claims claims = mock(Claims.class);

        when(jwtUtil.substringToken(rawToken)).thenReturn(strippedToken);
        when(jwtUtil.extractClaims(strippedToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(refreshTokenService.getRefreshToken(1L)).thenReturn(storedToken);

        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> authService.refreshAccessToken(rawToken));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }
}
