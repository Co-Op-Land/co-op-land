package com.coop.presentation.playHistory.dto.response;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.player.entity.Player;
import com.coop.presentation.member.dto.response.MemberResponse;
import lombok.Builder;

import java.util.List;

@Builder
public record HistoryResponse(
        Long historyId,
        List<MemberResponse> members
) {

    public static HistoryResponse from(History history, List<Player> players) {
        return HistoryResponse.builder()
                .historyId(history.getId())
                .members(players.stream()
                        .map(player -> {
                            var member = player.getMember();
                            return MemberResponse.from(member.getId(), null, member.getNickname());
                        })
                        .toList())
                .build();
    }
}

