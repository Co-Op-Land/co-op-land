package com.coop.presentation.room.dto.response;

import com.coop.domain.member.entity.Member;
import com.coop.domain.room.entity.Room;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record RoomReadDetailResponse(
        RoomInfoResponse roomInfo,
        List<PlayerInfoResponse> roomPlayers
) {

    public static RoomReadDetailResponse of(Room room, List<Member> players) {
        return RoomReadDetailResponse.builder()
                .roomInfo(RoomInfoResponse.of(room, players.size()))
                .roomPlayers(players.stream().map(PlayerInfoResponse::from).toList())
                .build();
    }
}
