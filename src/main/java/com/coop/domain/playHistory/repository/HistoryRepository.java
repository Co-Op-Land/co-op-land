package com.coop.domain.playHistory.repository;

import com.coop.domain.playHistory.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {
    Optional<History> findByRoomId(Long roomId);
    void removeByRoomId(Long roomId);
}
