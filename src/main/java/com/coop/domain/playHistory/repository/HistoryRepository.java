package com.coop.domain.playHistory.repository;

import com.coop.domain.playHistory.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
