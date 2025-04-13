package com.coop.presentation.notification.dto.response;

import lombok.Builder;

@Builder
public record NotificationResponse(
        String target,
        Long relatedId
) {
    public static NotificationResponse from(String target, Long relatedId) {
        return NotificationResponse.builder()
                .target(target)
                .relatedId(relatedId)
                .build();
    }
}
