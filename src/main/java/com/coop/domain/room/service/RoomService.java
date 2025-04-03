package com.coop.domain.room.service;

import com.coop.domain.game.entity.Game;
import com.coop.domain.game.repository.GameRepository;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.room.entity.Room;
import com.coop.domain.room.repository.RoomRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.ForbiddenException;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.room.dto.request.RoomCreateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateStatusRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;

    @Transactional
    public Long generateRoom(Long userId, RoomCreateRequest request) {
        Member member = findMemberById(userId);
        Game game = findGameById(request.gameId());

        Room room = roomRepository.save(request.toEntity(member, game));

        return room.getId();
    }

    public void modifyRoom(Long userId, Long roomId, RoomUpdateRequest request) {
        Room room = findRoomById(roomId);

        checkUserAuthority(room.getHost().getId(), userId);

        room.update(request.title(), request.maxPlayerCount(), request.difficulty(), request.visibility());
    }

    public void modifyRoomStatus(Long userId, Long roomId, RoomUpdateStatusRequest request) {
        Room room = findRoomById(roomId);

        checkUserAuthority(room.getHost().getId(), userId);

        room.updateStatus(request.status());
    }

    public Room findRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ROOM_NOT_FOUND));
    }

    // 헬퍼
    private Member findMemberById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private Game findGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GAME_NOT_FOUND));
    }

    private void checkUserAuthority(Long userId, Long loginUserId) {
        if (!userId.equals(loginUserId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
    }
}
