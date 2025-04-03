package com.coop.domain.player.repository;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.player.entity.Player;

import java.util.List;
import java.util.Map;

public interface PlayerRepositoryCustom {
    Map<History, List<Player>> findHistoriesWithPlayersByMemberId(Long memberId);
}
