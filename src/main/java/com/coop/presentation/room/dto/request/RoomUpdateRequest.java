package com.coop.presentation.room.dto.request;

import com.coop.domain.room.enums.Difficulty;

public record RoomUpdateRequest(
        String title,
        int maxPlayerCount,
        Difficulty difficulty
) {
}
