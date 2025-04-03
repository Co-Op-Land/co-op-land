package com.coop.presentation.playHistory.dto.response;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.player.entity.Player;
import lombok.Builder;

import java.util.List;
@Builder
public record HistoryResponse(Long historyId, List<String> nickname) {//여기에 memberId 대신에 dto를 넣어서 memberId, memberNickname정도를 넣고 싶어요.
    public static HistoryResponse from(History history, List<Player> players) {
        return HistoryResponse.builder()
                .historyId(history.getId())
                .nickname(players.stream()
                        .map(player -> player.getMember().getNickname())
                        .toList())
                .build();
    }
}
