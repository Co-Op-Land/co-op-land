package com.coop.domain.room.service;

import com.coop.domain.game.entity.Game;
import com.coop.domain.game.repository.GameRepository;
import com.coop.domain.member.entity.Member;
import com.coop.domain.member.repository.MemberRepository;
import com.coop.domain.room.entity.Room;
import com.coop.domain.room.repository.RoomRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.room.dto.request.RoomCreateRequest;
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
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Game game = gameRepository.findById(request.gameId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.GAME_NOT_FOUND));

        Room room = roomRepository.save(request.toEntity(member, game));

        return room.getId();
    }
}
