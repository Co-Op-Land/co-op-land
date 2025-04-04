package com.coop.global.repository;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.EntityAlreadyExistException;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.exception.error.NotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomPlayerRepository {

    private final Map<Long, Set<Long>> roomPlayers = new ConcurrentHashMap<>();
    private final Map<Long, Long> playerToRoom = new ConcurrentHashMap<>();

    public void addPlayer(Long roomId, Long memberId, Integer maxPlayerCount) {
        if (isPlayerInAnyRoom(memberId)) {
            throw new EntityAlreadyExistException(ErrorCode.MEMBER_ALREADY_IN_ROOM);
        }

        Set<Long> playerInRoom = roomPlayers.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet());

        if (maxPlayerCount <= playerInRoom.size()) {
            throw new InvalidRequestException(ErrorCode.ROOM_IS_FULL);
        }

        playerInRoom.add(memberId);
        playerToRoom.put(memberId, roomId);
    }

    public void removePlayer(Long roomId, Long memberId) {
        if (!isPlayerInThisRoom(memberId, roomId)) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_IN_ROOM);
        }

        roomPlayers.computeIfPresent(roomId, (k, members) -> {
            members.remove(memberId);
            return members.isEmpty() ? null : members;
        });
        playerToRoom.remove(memberId);
    }

    public Set<Long> getPlayerInRoom(Long roomId) {
        if (!isRoomExists(roomId)) {
            throw new NotFoundException(ErrorCode.ROOM_NOT_FOUND);
        }

        return roomPlayers.get(roomId);
    }

    public boolean isRoomExists(Long roomId) {
        return roomPlayers.containsKey(roomId);
    }

    private boolean isPlayerInAnyRoom(Long memberId) {
        return playerToRoom.containsKey(memberId);
    }

    private boolean isPlayerInThisRoom(Long memberId, Long roomId) {
        return roomPlayers.getOrDefault(roomId, Collections.emptySet())
                .contains(memberId);
    }
}
