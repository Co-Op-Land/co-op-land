package com.coop.presentation.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RefreshAccessTokenResponse(
        String accessToken, String refreshToken
) {
    public static RefreshAccessTokenResponse from(String accessToken, String refreshToken) {
        return RefreshAccessTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
