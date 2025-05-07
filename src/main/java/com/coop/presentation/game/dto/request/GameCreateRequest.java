package com.coop.presentation.game.dto.request;

import com.coop.domain.game.entity.Game;

public record GameCreateRequest(
        String name,
        Integer maxPlayerCounts
) {

    public Game toEntity() {
        return Game.builder()
                .name(this.name)
                .maxPlayerCounts(this.maxPlayerCounts)
                .build();
    }
}
