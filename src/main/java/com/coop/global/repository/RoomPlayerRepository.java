package com.coop.global.repository;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.EntityAlreadyExistException;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomPlayerRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String MATCHING_KEY = "match:rooms:";
    private final String ROOM_KEY = "room:players:";
    private final String ROOM_INFO_KEY = "room:info:";
    private final String MEMBER_KEY = "member:in-room:";

    public void generateRoom(Long roomId, Long memberId, Long gameId, Integer maxPlayerCount) {
        isPlayerInAnyRoom(memberId);

        addPlayerToRoom(roomId, memberId, gameId, maxPlayerCount);
        redisTemplate.opsForValue().set(ROOM_INFO_KEY + roomId, maxPlayerCount);
    }

    public void joinRoom(Long roomId, Long memberId, Long gameId) {
        isPlayerInAnyRoom(memberId);
        Integer maxPlayerCount = getMaxPlayerCount(roomId);

        if (maxPlayerCount <= getCurrentPlayerCount(roomId)) {
            throw new InvalidRequestException(ErrorCode.ROOM_IS_FULL);
        }

        addPlayerToRoom(roomId, memberId, gameId, maxPlayerCount);
    }

    public void removePlayer(Long roomId, Long memberId) {
        isPlayerInThisRoom(roomId, memberId);

        String roomKey = ROOM_KEY + roomId;

        redisTemplate.opsForSet().remove(roomKey, String.valueOf(memberId));
        redisTemplate.delete(MEMBER_KEY + memberId);

        Long remainingPlayers = redisTemplate.opsForSet().size(roomKey);

        if (remainingPlayers == null || remainingPlayers == 0) {
            redisTemplate.delete(roomKey);
            redisTemplate.delete(ROOM_INFO_KEY + roomId);
        }
    }

    public Set<Long> getPlayerInRoom(Long roomId) {
        if (!isRoomExists(roomId)) {
            throw new NotFoundException(ErrorCode.REDIS_ROOM_NOT_FOUND);
        }

        return Optional.ofNullable(redisTemplate.opsForSet().members(ROOM_KEY + roomId))
                .orElse(Collections.emptySet())
                .stream()
                .map(playerId -> Long.valueOf(playerId.toString()))
                .collect(Collectors.toSet());
    }

    public Set<Long> getMatchingRooms(Long gameId) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rangeByScore(MATCHING_KEY + gameId, 0, -1))
                .stream()
                .map(roomId -> Long.valueOf(roomId.toString()))
                .collect(Collectors.toSet());
    }

    public Integer getCurrentPlayerCount(Long roomId) {
        return Optional.ofNullable(redisTemplate.opsForSet().size(ROOM_KEY + roomId))
                .map(Long::intValue)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_ROOM_NOT_FOUND));
    }

    public Integer getMaxPlayerCount(Long roomId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(ROOM_INFO_KEY + roomId))
                .map(o -> Integer.valueOf(o.toString()))
                .orElseThrow(() -> new NotFoundException(ErrorCode.REDIS_ROOM_NOT_FOUND));
    }

    public boolean isRoomExists(Long roomId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(ROOM_KEY + roomId));
    }

    private void isPlayerInAnyRoom(Long memberId) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(MEMBER_KEY + memberId))) {
            throw new EntityAlreadyExistException(ErrorCode.MEMBER_ALREADY_IN_ROOM);
        }
    }

    private void isPlayerInThisRoom(Long roomId, Long memberId) {
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(ROOM_KEY + roomId, String.valueOf(memberId)))) {
            throw new NotFoundException(ErrorCode.MEMBER_NOT_IN_ROOM);
        }
    }

    private void addPlayerToRoom(Long roomId, Long memberId, Long gameId, Integer maxPlayerCount) {
        redisTemplate.opsForSet().add(ROOM_KEY + roomId, String.valueOf(memberId));
        redisTemplate.opsForValue().set(MEMBER_KEY + memberId, true);

        Integer currentPlayerCount = getCurrentPlayerCount(roomId);
        redisTemplate.opsForZSet().add(
                MATCHING_KEY + gameId,
                String.valueOf(roomId),
                maxPlayerCount - currentPlayerCount
        );
    }
}
