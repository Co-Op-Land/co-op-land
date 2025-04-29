package com.coop.global.repository;

import com.coop.domain.room.entity.RoomInfo;
import com.coop.domain.room.enums.Visibility;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.EntityAlreadyExistException;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomPlayerService {

    private final RoomPlayerRepository roomPlayerRepository;
    private final RoomRedisRepository roomRedisRepository;

    public void generateRoom(Long roomId, Long memberId, Long gameId, Integer maxPlayerCount, Visibility visibility) {
        addPlayerToRoom(roomId, memberId, gameId, maxPlayerCount, visibility);

        RoomInfo roomInfo = RoomInfo.builder()
                .roomId(roomId)
                .maxPlayerCount(maxPlayerCount)
                .gameId(gameId)
                .visibility(visibility)
                .build();

        roomRedisRepository.save(roomInfo);
    }

    public void joinRoom(Long roomId, Long memberId) {
        RoomInfo roomInfo = findRoomInfoById(roomId);
        Integer maxPlayerCount = roomInfo.getMaxPlayerCount();

        if (maxPlayerCount <= getCurrentPlayerCount(roomId)) {
            throw new InvalidRequestException(ErrorCode.ROOM_IS_FULL);
        }

        addPlayerToRoom(roomId, memberId, roomInfo.getGameId(), maxPlayerCount, roomInfo.getVisibility());
    }

    public void removePlayer(Long roomId, Long memberId) {
        checkPlayerInThisRoom(roomId, memberId);

        roomPlayerRepository.removePlayerInRoom(roomId, memberId);
        roomPlayerRepository.deletePlayer(memberId);

        Long remainingPlayers = roomPlayerRepository.getCurrentPlayerCountByRoomId(roomId);

        if (remainingPlayers == null || remainingPlayers == 0) {
            RoomInfo roomInfo = findRoomInfoById(roomId);

            roomPlayerRepository.deleteRoom(roomId);
            roomPlayerRepository.removeMatching(roomInfo.getGameId(), roomId.toString());
            roomRedisRepository.delete(roomInfo);
        }
    }

    public Set<Long> getPlayersInRoom(Long roomId) {
        if (!isRoomExists(roomId)) {
            throw new NotFoundException(ErrorCode.REDIS_ROOM_NOT_FOUND);
        }

        return Optional.ofNullable(roomPlayerRepository.getPlayersByRoomId(roomId))
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_ROOM_NO_PLAYERS))
                .stream()
                .map(playerId -> Long.valueOf(playerId.toString()))
                .collect(Collectors.toSet());
    }

    public Integer getCurrentPlayerCount(Long roomId) {
        return Optional.ofNullable(roomPlayerRepository.getCurrentPlayerCountByRoomId(roomId))
                .map(Long::intValue)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_ROOM_NO_PLAYERS));
    }

    public Set<Long> getMatchingRooms(Long gameId) {
        return Optional.ofNullable(roomPlayerRepository.getMatchingRoomsByGameId(gameId))
                .stream()
                .map(roomId -> Long.valueOf(roomId.toString()))
                .collect(Collectors.toSet());
    }

    public void updateRoomInfo(
            Long roomId,
            Integer maxPlayerCount,
            Visibility visibility
    ) {
        RoomInfo roomInfo = findRoomInfoById(roomId);
        Integer currentPlayerCount = getCurrentPlayerCount(roomId);

        if (maxPlayerCount != null) {
            if (currentPlayerCount > maxPlayerCount) {
                throw new InvalidRequestException(ErrorCode.EXCEEDS_CURRENT_PLAYER_COUNT);
            }

            roomInfo.updateMaxPlayerCount(maxPlayerCount);
        }

        if (visibility != null && visibility != roomInfo.getVisibility()) {
            if (visibility == Visibility.PUBLIC) {
                roomPlayerRepository.saveMatching(roomInfo.getGameId(), roomId.toString(), currentPlayerCount);
            } else {
                roomPlayerRepository.removeMatching(roomInfo.getGameId(), roomId.toString());
            }

            roomInfo.updateVisibility(visibility);
        }

        roomRedisRepository.save(roomInfo);
    }

    public boolean isRoomExists(Long roomId) {
        return roomPlayerRepository.roomExists(roomId);
    }

    public void checkPlayerInAnyRoom(Long memberId) {
        if (roomPlayerRepository.playerExists(memberId)) {
            throw new EntityAlreadyExistException(ErrorCode.PLAYER_ALREADY_IN_ROOM);
        }
    }

    private void checkPlayerInThisRoom(Long roomId, Long memberId) {
        if (!roomPlayerRepository.isPlayerInRoom(roomId, memberId)) {
            throw new NotFoundException(ErrorCode.PLAYER_NOT_IN_ROOM);
        }
    }

    private RoomInfo findRoomInfoById(Long roomId) {
        return roomRedisRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_ROOM_NOT_FOUND));
    }

    private void addPlayerToRoom(
            Long roomId,
            Long memberId,
            Long gameId,
            Integer maxPlayerCount,
            Visibility visibility
    ) {
        roomPlayerRepository.savePlayerToRoom(roomId, memberId);
        roomPlayerRepository.savePlayer(memberId);

        Integer currentPlayerCount = getCurrentPlayerCount(roomId);

        if (visibility == Visibility.PUBLIC) {
            roomPlayerRepository.saveMatching(
                    gameId,
                    roomId.toString(),
                    maxPlayerCount - currentPlayerCount
            );
        }
    }
}
