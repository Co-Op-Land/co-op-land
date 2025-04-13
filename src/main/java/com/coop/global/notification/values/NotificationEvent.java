package com.coop.global.notification.values;

import com.coop.domain.notification.enums.NotificationTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private NotificationTarget target;
    private Long fromMemberId;
    private Long toMemberId;
    private Long relatedId;
    private String content;
}
