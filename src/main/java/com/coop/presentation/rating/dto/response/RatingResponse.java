package com.coop.presentation.rating.dto.response;

import com.coop.domain.rating.entity.Rating;
import lombok.Builder;

@Builder
public record RatingResponse (String fromMemberNickname, int score, String reason) {

    public static RatingResponse from(Rating rating) {
        return RatingResponse.builder()
                .fromMemberNickname(rating.getFromMember().getNickname())
                .score(rating.getScore())
                .reason(rating.getReason())
                .build();
    }
}
