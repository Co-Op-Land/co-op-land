package com.coop.presentation.room.dto.request;

import com.coop.domain.game.entity.Game;
import com.coop.domain.member.entity.Member;
import com.coop.domain.room.entity.Room;
import com.coop.domain.room.enums.Difficulty;
import com.coop.domain.room.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(
        @NotBlank
        String title,

        @NotNull
        int maxPlayerCount,

        @NotNull
        Difficulty difficulty,

        @NotNull
        Status status,

        @NotNull
        Long gameId
) {

    public Room toEntity(Member member, Game game) {
        return Room.builder()
                .title(this.title)
                .maxPlayerCount(this.maxPlayerCount)
                .difficulty(this.difficulty)
                .status(this.status)
                .member(member)
                .game(game)
                .build();
    }
}
