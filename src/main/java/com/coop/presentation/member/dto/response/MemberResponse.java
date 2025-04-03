package com.coop.presentation.member.dto.response;

import lombok.Builder;

@Builder
public record MemberResponse(
        String email,
        String name
) {
    public static MemberResponse from(String email, String name) {
        return MemberResponse.builder()
                .email(email)
                .name(name)
                .build();
    }
}
