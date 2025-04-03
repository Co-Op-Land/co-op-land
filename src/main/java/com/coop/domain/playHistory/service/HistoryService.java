package com.coop.domain.playHistory.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.repository.HistoryRepository;
import com.coop.domain.player.service.PlayerService;
import com.coop.domain.room.entity.Room;
import com.coop.presentation.playHistory.dto.response.HistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final PlayerService playerService;

    public History generateHistory(Room room, List<Member> members) {
        History history = History.builder()
                .room(room)
                .build();
        generatePlayer(history,members);
        return historyRepository.save(history);
    }

    private void generatePlayer(History history, List<Member> members) {
        playerService.generatePlayer(history,members);
    }

    public void findMemberHistories(Long memberId) {
    }
}
