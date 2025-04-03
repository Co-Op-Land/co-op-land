package com.coop.land.member;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.member.service.MemberService;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.land.util.TestUtils;
import com.coop.presentation.member.dto.request.UpdatePasswordRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdatePasswordTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final Long userId = 1L;
    private final String oldPassword = "oldPassword123";
    private final String newPassword = "newPassword123";
    private final String encodedPassword = "encodedNewPassword123";
    private final String email = "test@example.com";

    @Test
    void updatePassword_성공() {
        UpdatePasswordRequest requestDto = new UpdatePasswordRequest(oldPassword, newPassword);
        Member mockMember = TestUtils.createEntity(Member.class, Map.of(
                "email", email,
                "password", encodedPassword
        ));

        when(memberRepository.findById(userId)).thenReturn(Optional.of(mockMember));
        when(passwordEncoder.matches(oldPassword, mockMember.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        memberService.updatePassword(userId, requestDto);

        assertEquals(encodedPassword, mockMember.getPassword());
        verify(passwordEncoder, times(1)).matches(oldPassword, mockMember.getPassword());
        verify(passwordEncoder, times(1)).encode(newPassword);
    }

    @Test
    void updatePassword_현재비밀번호_불일치() {
        UpdatePasswordRequest requestDto = new UpdatePasswordRequest("wrongPassword", newPassword);
        Member mockUser = TestUtils.createEntity(Member.class, Map.of(
                "email", email,
                "password", encodedPassword
        ));

        when(memberRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongPassword", mockUser.getPassword())).thenReturn(false);

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                memberService.updatePassword(userId, requestDto));

        assertEquals("패스워드가 잘못되었습니다.", exception.getMessage());
        verify(passwordEncoder, times(1)).matches("wrongPassword", mockUser.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }
}
