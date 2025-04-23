package com.coop.domain.matching.service;

import com.coop.global.exception.error.BaseException;
import com.coop.global.repository.RoomPlayerRepository;
import com.coop.presentation.matching.dto.request.MatchingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {

    private final RoomPlayerRepository roomPlayerRepository;

    public void joinMatching(Long memberId, MatchingRequest request) {
        Set<Long> matchingRoomIds = roomPlayerRepository.getMatchingRooms(request.gameId());

        for (Long roomId : matchingRoomIds) {
            try {
                roomPlayerRepository.joinRoom(
                        roomId,
                        memberId,
                        request.gameId()
                );
                break;
            } catch (BaseException e) {
                log.error("매칭 중 에러 발생: Error Code: {}, Message: {}", e.getErrorCode().getCode(), e.getMessage());
            }
        }
    }
}
