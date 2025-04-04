package com.coop.presentation.room.dto.response;

import com.coop.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PlayerInfoResponse(
        Long memberId,
        String nickname,
        Double rating
) {

    public static PlayerInfoResponse from(Member member) {
        return PlayerInfoResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
//                .rating(member.getRating)
                .build();
    }
}
