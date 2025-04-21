package com.coop.presentation.admin;

import lombok.Builder;

import java.util.List;

@Builder
public record WebSocketIdListResponse(
        List<Long> ids,
        int size
) {
    public static WebSocketIdListResponse from(List<Long> ids) {
        return WebSocketIdListResponse.builder()
                .ids(ids)
                .size(ids.size())
                .build();
    }
}
