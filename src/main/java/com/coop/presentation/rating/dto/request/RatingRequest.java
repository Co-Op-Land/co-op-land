package com.coop.presentation.rating.dto.request;

import com.coop.domain.member.entity.Member;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.rating.entity.Rating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record RatingRequest(
        @NotNull
        Long historyId,
        @NotNull
        Long toMemberId,
        @NotNull
        int score,
        @NotBlank
        String reason
) {
    public Rating toEntity(Member fromMember, History history, Member toMember) {
        return Rating.builder()
                .history(history)
                .toMember(toMember)
                .fromMember(fromMember)
                .score(score)
                .reason(reason)
                .build();
    }
}
