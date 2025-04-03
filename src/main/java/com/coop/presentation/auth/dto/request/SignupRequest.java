package com.coop.presentation.auth.dto.request;

import com.coop.domain.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
        @Email String email,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "올바른 비밀번호가 아닙니다.") @NotBlank String password,
        @NotBlank String nickname
) {

    public static Member toEntity(String email, String nickname, String password) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
