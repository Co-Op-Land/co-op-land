package com.coop.presentation.room.dto.response;

import com.coop.domain.room.entity.Room;
import com.coop.domain.room.enums.Difficulty;
import com.coop.domain.room.enums.Status;
import com.coop.domain.room.enums.Visibility;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record RoomInfoResponse(
        Long roomId,
        String title,
        String gameName,
        Integer currentPlayerCount,
        Integer maxPlayerCount,
        Difficulty difficulty,
        Status status,
        Visibility visibility
) {

    public static RoomInfoResponse of(Room room, Integer currentPlayerCount) {
        return RoomInfoResponse.builder()
                .roomId(room.getId())
                .title(room.getTitle())
                .gameName(room.getGame().getName())
                .currentPlayerCount(currentPlayerCount)
                .maxPlayerCount(room.getMaxPlayerCount())
                .difficulty(room.getDifficulty())
                .status(room.getStatus())
                .visibility(room.getVisibility())
                .build();
    }
}
