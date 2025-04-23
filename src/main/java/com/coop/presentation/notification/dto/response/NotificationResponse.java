package com.coop.presentation.notification.dto.response;

import com.coop.domain.notification.entity.NotificationRecipient;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationResponse(
        Long notificationId,
        String target,
        Long relatedId,
        boolean isRead,
        String content,
        String endPoint
) {
    public static NotificationResponse from(NotificationRecipient recipient) {
        return NotificationResponse.builder()
                .notificationId(recipient.getNotification().getId())
                .target(recipient.getNotification().getTarget().toString().toLowerCase())
                .relatedId(recipient.getNotification().getRelatedId())
                .isRead(recipient.isRead())
                .content(recipient.getNotification().getContent())
                .endPoint(recipient.getNotification().getEndPoint())
                .build();
    }
}
