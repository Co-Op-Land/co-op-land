package com.coop.presentation.admin;

import lombok.Builder;

import java.util.List;

@Builder
public record AdminNotificationResponse(
        List<Long> ids, //TODO: 이거 response 말고 strategy 바로가져오면 좋을듯
        String response
) {
    public static AdminNotificationResponse from(List<Long> ids, String content) {
        return AdminNotificationResponse.builder()
                .ids(ids)
                .response("유저 " + ids.size() + "명에게 " + content + " 전송 성공")
                .build();
    }
}
