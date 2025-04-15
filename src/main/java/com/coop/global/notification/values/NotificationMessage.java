package com.coop.global.notification.values;

import com.coop.domain.notification.enums.NotificationTarget;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationMessage(  //websocket 발송용 메세지 객체
        NotificationTarget target,
        Long relatedId,
        String content,
        String endPoint
) {
    public static NotificationMessage from(NotificationEvent event) {
        return NotificationMessage.builder()
                .target(event.target())
                .relatedId(event.relatedId())
                .content(event.content())
                .endPoint(event.endPoint())
                .build();
    }
}
