package com.coop.domain.playHistory.service;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryComponent {

    private final HistoryRepository historyRepository;

    public History findById(Long id) {
        return historyRepository.findById(id).orElseThrow();
    }
}
