package com.coop.presentation.room.dto.request;

import com.coop.domain.room.enums.Difficulty;
import com.coop.domain.room.enums.Visibility;

public record RoomUpdateRequest(
        String title,
        Integer maxPlayerCount,
        Difficulty difficulty,
        Visibility visibility
) {
}
