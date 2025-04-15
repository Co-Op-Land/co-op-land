package com.coop.presentation.admin;

import jakarta.validation.constraints.NotBlank;

public record AdminNotificationRequest(
        Long id,
        @NotBlank String content
) {
}
