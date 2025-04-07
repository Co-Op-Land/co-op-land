package com.coop.domain.playHistory.service;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.repository.HistoryRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HistoryComponent {

    private final HistoryRepository historyRepository;

    public History findById(Long id) {
        return historyRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(ErrorCode.HISTORY_NOT_FOUND));
    }
}
