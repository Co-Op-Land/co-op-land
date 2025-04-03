package com.coop.land.auth;

import com.coop.domain.auth.service.AuthService;
import com.coop.domain.auth.service.RefreshTokenService;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RefreshAccessTokenTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRefreshAccessToken_액세스토큰_재발급_성공했을때() {
        //given
        String refreshToken = "validRefreshToken";
        Claims claims = mock(Claims.class);
        Member member = mock(Member.class);

        when(jwtUtil.extractClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(refreshTokenService.getRefreshToken(1L)).thenReturn("validRefreshToken");
        when(jwtUtil.substringToken(refreshToken)).thenReturn("validRefreshToken");
        when(jwtUtil.substringToken("validRefreshToken")).thenReturn("validRefreshToken");
        when(jwtUtil.createRefreshToken(1L)).thenReturn("newRefreshToken");
        when(memberRepository.findById(1L)).thenReturn(Optional.ofNullable(member));
        when(jwtUtil.createToken(1L, member.getRole())).thenReturn("newAccessToken");

        //when
        RefreshAccessTokenResponse response = authService.refreshAccessToken(refreshToken);

        //then
        assertNotNull(response);
        assertEquals("newAccessToken", response.accessToken());
    }

    @Test
    void testRefreshAccessToken_존재하지_않는_토큰() {
        //given
        String invalidRefreshToken = "invalidToken";
        when(jwtUtil.extractClaims(invalidRefreshToken)).thenThrow(new UnAuthorizedException(ErrorCode.TOKEN_UNAUTHORIZED));

        //when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> authService.refreshAccessToken(invalidRefreshToken));
        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }

    @Test
    void testRefreshAccessToken_리프레시토큰_불일치() {
        //given
        String refreshToken = "validRefreshToken";
        String storedToken = "differentStoredToken";
        Claims claims = mock(Claims.class);

        when(jwtUtil.extractClaims(refreshToken)).thenReturn(claims);
        when(claims.getSubject()).thenReturn("1");
        when(refreshTokenService.getRefreshToken(1L)).thenReturn(storedToken);

        //when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class,
                () -> authService.refreshAccessToken(refreshToken));

        assertEquals(ErrorCode.TOKEN_UNAUTHORIZED.getMessage(), exception.getMessage());
    }

}
