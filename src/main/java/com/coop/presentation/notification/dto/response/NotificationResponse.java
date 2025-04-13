package com.coop.presentation.notification.dto.response;

import com.coop.domain.notification.entity.NotificationRecipient;
import lombok.Builder;

@Builder
public record NotificationResponse(
        Long notificationId,
        String target,
        Long relatedId,
        boolean isRead
) {
    public static NotificationResponse from(NotificationRecipient recipient) {
        return NotificationResponse.builder()
                .notificationId(recipient.getNotification().getId())
                .target(recipient.getNotification().getTarget().toString().toLowerCase())
                .relatedId(recipient.getNotification().getRelatedId())
                .isRead(recipient.isRead())
                .build();
    }
}
