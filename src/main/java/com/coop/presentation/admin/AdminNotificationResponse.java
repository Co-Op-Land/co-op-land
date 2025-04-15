package com.coop.presentation.admin;

import lombok.Builder;

import java.util.List;

@Builder
public record AdminNotificationResponse(
        String content
) {
    public static AdminNotificationResponse from(List<Long> ids, String content) {
        return AdminNotificationResponse.builder()
                .content("유저 " + ids.size() + "명에게 " + content + " 전송 성공")
                .build();
    }
}
