package com.coop.domain.room.entity;

import com.coop.domain.room.enums.Visibility;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "room:info", timeToLive = 21600)
@Getter
public class RoomInfo {

    @Id
    private Long roomId;
    private Integer maxPlayerCount;
    private Long gameId;
    private Visibility visibility;

    @Builder
    public RoomInfo(Long roomId, Integer maxPlayerCount, Long gameId, Visibility visibility) {
        this.roomId = roomId;
        this.maxPlayerCount = maxPlayerCount;
        this.gameId = gameId;
        this.visibility = visibility;
    }

    public void updateMaxPlayerCount(Integer maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public void updateVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
