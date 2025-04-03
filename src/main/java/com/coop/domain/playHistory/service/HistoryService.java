package com.coop.domain.playHistory.service;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.repository.HistoryRepository;
import com.coop.domain.room.entity.Room;
import com.coop.domain.room.repository.RoomRepository;
import com.coop.global.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final RoomRepository roomRepository;

    public History generateHistory(Long room_id) {
        Room room = roomRepository.findById(room_id).orElseThrow(NotFoundException::new);
        History history = History.builder()
                .room(room)
                .build();
        return historyRepository.save(history);
    }
}
