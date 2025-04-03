package com.coop.presentation.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequest(
        String oldPassword,
        @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$", message = "올바른 비밀번호가 아닙니다.") @NotBlank String newPassword
) {
}
