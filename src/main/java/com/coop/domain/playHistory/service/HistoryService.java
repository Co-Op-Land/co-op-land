package com.coop.domain.playHistory.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.repository.HistoryRepository;
import com.coop.domain.player.entity.Player;
import com.coop.domain.player.service.PlayerService;
import com.coop.domain.room.entity.Room;
import com.coop.global.exception.error.NotFoundException;
import com.coop.presentation.playHistory.dto.response.HistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final PlayerService playerService;

    public History generateHistory(Room room, List<Member> members) {
        History history = History.builder()
                .room(room)
                .isCompleted(false)
                .build();
        generatePlayer(history,members);
        return historyRepository.save(history);
    }

    @Transactional
    public void modifyHistoryToCompleted(Long roomId) {//room id로
        History history = historyRepository.findByRoomId(roomId).orElseThrow(NotFoundException::new);
        history.ProcessHistoryComplete();
    }

    private void generatePlayer(History history, List<Member> members) {
        playerService.generatePlayer(history,members);
    }

    public List<HistoryResponse> findMemberHistories(Long memberId) {
        Map<History, List<Player>> gameHistories = playerService.findPlayersFromHistoriesByMember(memberId);
        return transformToResponseDto(gameHistories);
    }

    private static List<HistoryResponse> transformToResponseDto(Map<History, List<Player>> gameHistories) {
        return gameHistories.entrySet().stream()
                .map(entry -> HistoryResponse.from(entry.getKey(), entry.getValue()))
                .toList();
    }
}
