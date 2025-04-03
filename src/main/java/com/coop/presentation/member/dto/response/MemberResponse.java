package com.coop.presentation.member.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MemberResponse(
        Long id,
        String email,
        String name
) {
    public static MemberResponse from(Long id, String email, String name) {
        return MemberResponse.builder()
                .id(id)
                .email(email)
                .name(name)
                .build();
    }
}
