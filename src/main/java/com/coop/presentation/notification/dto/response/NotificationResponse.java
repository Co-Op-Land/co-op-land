package com.coop.presentation.notification.dto.response;

import lombok.Builder;

@Builder
public record NotificationResponse(
        Long notificationId,
        String target,
        Long relatedId,
        boolean isRead
) {
    public static NotificationResponse from(Long notificationId, String target, Long relatedId, boolean isRead) {
        return NotificationResponse.builder()
                .notificationId(notificationId)
                .target(target)
                .relatedId(relatedId)
                .isRead(isRead)
                .build();
    }
}
