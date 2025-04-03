package com.coop.presentation.rating.dto.request;

import com.coop.domain.member.entity.Member;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.rating.entity.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public class RatingRequest {
    @NotNull
    private History history;
    @NotNull
    private Member toMember;
    @NotNull
    private int score;
    @NotBlank
    private String reason;

    public Rating toEntity(Member fromMember) {
        return Rating.builder()
                .history(history)
                .toMember(toMember)
                .fromMember(fromMember)
                .score(score)
                .reason(reason)
                .build();
    }
}
