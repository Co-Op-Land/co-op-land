package com.coop.presentation.room.dto.response;

import com.coop.domain.room.entity.Room;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record RoomReadResponse(
        RoomInfoResponse roomInfo
) {

    public static RoomReadResponse of(Room room, Integer currentPlayerCount) {
        return RoomReadResponse.builder()
                .roomInfo(RoomInfoResponse.of(room, currentPlayerCount))
                .build();
    }
}
