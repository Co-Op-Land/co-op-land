package com.coop.domain.room.service;

import com.coop.domain.game.entity.Game;
import com.coop.domain.game.service.GameComponent;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.playHistory.service.HistoryService;
import com.coop.domain.room.entity.Room;
import com.coop.domain.room.repository.RoomRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.ForbiddenException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.global.repository.RoomPlayerRepository;
import com.coop.presentation.room.dto.request.RoomCreateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateStatusRequest;
import com.coop.presentation.room.dto.response.RoomReadDetailResponse;
import com.coop.presentation.room.dto.response.RoomReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final MemberComponent memberComponent;
    private final GameComponent gameComponent;
    private final HistoryService historyService;
    private final RoomRepository roomRepository;
    private final RoomPlayerRepository roomPlayerRepository;

    @Transactional
    public Long generateRoom(Long memberId, RoomCreateRequest request) {
        Member member = memberComponent.findById(memberId);
        Game game = gameComponent.findGameById(request.gameId());

        Room room = roomRepository.save(request.toEntity(member, game));
        roomPlayerRepository.generateRoom(
                room.getId(),
                memberId,
                game.getId(),
                room.getMaxPlayerCount()
        );

        return room.getId();
    }

    @Transactional(readOnly = true)
    public List<RoomReadResponse> findRooms() {
        List<Room> rooms = roomRepository.findAll();
        Map<Long, Integer> currentPlayerCountInRoom = rooms.stream()
                .collect(Collectors.toMap(
                        Room::getId,
                        room -> roomPlayerRepository.getPlayerInRoom(room.getId()).size())
                );

        return rooms.stream()
                .map(room -> RoomReadResponse.of(room, currentPlayerCountInRoom.get(room.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomReadDetailResponse findRoom(Long roomId) {
        Room room = findRoomById(roomId);
        Set<Long> playerInRoom = roomPlayerRepository.getPlayerInRoom(room.getId());
        List<Member> players = memberComponent.getMembers(playerInRoom);

        return RoomReadDetailResponse.of(room, players);
    }

    @Transactional
    public void modifyRoom(Long memberId, Long roomId, RoomUpdateRequest request) {
        Room room = findRoomById(roomId);

        checkUserAuthority(room.getHost().getId(), memberId);

        room.update(request.title(), request.maxPlayerCount(), request.difficulty(), request.visibility());
    }

    @Transactional
    public void modifyRoomStatus(Long memberId, Long roomId, RoomUpdateStatusRequest request) {
        Room room = findRoomById(roomId);

        checkUserAuthority(room.getHost().getId(), memberId);

        Set<Long> playerInRoom = roomPlayerRepository.getPlayerInRoom(room.getId());
        List<Member> players = memberComponent.getMembers(playerInRoom);

        switch (request.status()) {
            case PLAYING -> historyService.generateHistory(room, players);
            case COMPLETED -> historyService.modifyHistoryToCompleted(room.getId());
        }
        room.updateStatus(request.status());
    }

    @Transactional
    public void joinRoom(Long memberId, Long roomId) {
        Room room = findRoomById(roomId);

        roomPlayerRepository.joinRoom(
                room.getId(),
                memberId,
                room.getGame().getId()
        );
    }

    @Transactional
    public void leaveRoom(Long memberId, Long roomId) {
        Room room = findRoomById(roomId);

        roomPlayerRepository.removePlayer(room.getId(), memberId);

        if (!roomPlayerRepository.isRoomExists(roomId)) {
            room.close();
        }
    }

    @Transactional(readOnly = true)
    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
    }

    // 헬퍼

    private void checkUserAuthority(Long userId, Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
    }
}
