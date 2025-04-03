package com.coop.land.auth;

import com.coop.domain.auth.service.AuthService;
import com.coop.domain.auth.service.RefreshTokenService;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.security.JwtUtil;
import com.coop.presentation.auth.dto.request.LoginRequest;
import com.coop.presentation.auth.dto.response.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testLogin_로그인_성공했을때() {
        //given
        LoginRequest dto = new LoginRequest("test@example.com", "password");
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(dto.email())).thenReturn(Optional.ofNullable(member));
        when(passwordEncoder.matches(dto.password(), member.getPassword())).thenReturn(true);
        when(jwtUtil.createToken(anyLong(), any())).thenReturn("accessToken");
        when(jwtUtil.createRefreshToken(anyLong())).thenReturn("refreshToken");
        doNothing().when(refreshTokenService).createRefreshToken(anyLong(), anyString());

        //when
        LoginResponse response = authService.login(dto);

        //then
        assertNotNull(response);
        assertEquals("accessToken", response.accessToken());
        assertEquals("refreshToken", response.refreshToken());
    }

    @Test
    void testLogin_비밀번호_틀릴때() {
        //given
        LoginRequest dto = new LoginRequest("test@example.com", "wrongpassword");
        Member member = mock(Member.class);
        when(memberRepository.findByEmail(dto.email())).thenReturn(Optional.ofNullable(member));
        when(passwordEncoder.matches(dto.password(), member.getPassword())).thenReturn(false);

        //when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> authService.login(dto));
        assertEquals(ErrorCode.INVALID_PASSWORD.getMessage(), exception.getMessage());
    }
}
