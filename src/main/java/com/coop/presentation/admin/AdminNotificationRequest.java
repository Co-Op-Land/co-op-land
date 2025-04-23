package com.coop.presentation.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdminNotificationRequest(
        @NotNull long id,
        @NotBlank String content,
        String endPoint   //알림 리디렉션 엔드포인트
) {
}
