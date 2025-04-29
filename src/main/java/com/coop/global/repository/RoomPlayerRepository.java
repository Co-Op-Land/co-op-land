package com.coop.global.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoomPlayerRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String MATCHING_KEY = "match:rooms:";
    private final String ROOM_KEY = "room:players:";
    private final String PLAYER_KEY = "player:in-room:";

    public boolean playerExists(Long playerId) {
        return hasKey(PLAYER_KEY + playerId);
    }

    public boolean roomExists(Long roomId) {
        return hasKey(ROOM_KEY + roomId);
    }

    public boolean isPlayerInRoom(Long roomId, Long playerId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(ROOM_KEY + roomId, String.valueOf(playerId)));
    }

    public void savePlayerToRoom(Long roomId, Long playerId) {
        redisTemplate.opsForSet().add(ROOM_KEY + roomId, String.valueOf(playerId));
    }

    public void savePlayer(Long playerId) {
        redisTemplate.opsForValue().set(PLAYER_KEY + playerId, true);
    }

    public void saveMatching(Long gameId, String roomId, double count) {
        redisTemplate.opsForZSet().add(MATCHING_KEY + gameId, roomId, count);
    }

    public Set<Object> getPlayersByRoomId(Long roomId) {
        return redisTemplate.opsForSet().members(ROOM_KEY + roomId);
    }

    public Long getCurrentPlayerCountByRoomId(Long roomId) {
        return redisTemplate.opsForSet().size(ROOM_KEY + roomId);
    }

    public Set<Object> getMatchingRoomsByGameId(Long gameId) {
        return redisTemplate.opsForZSet().rangeByScore(MATCHING_KEY + gameId, 0, -1);
    }

    public void removePlayerInRoom(Long roomId, Long memberId) {
        redisTemplate.opsForSet().remove(ROOM_KEY + roomId, String.valueOf(memberId));
    }

    public void removeMatching(Long gameId, String roomId) {
        redisTemplate.opsForZSet().remove(MATCHING_KEY + gameId, String.valueOf(roomId));
    }

    public void deleteRoom(Long roomId) {
        delete(ROOM_KEY + roomId);
    }

    public void deletePlayer(Long memberId) {
        delete(PLAYER_KEY + memberId);
    }

    private boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private void delete(String key) {
        redisTemplate.delete(key);
    }
}
