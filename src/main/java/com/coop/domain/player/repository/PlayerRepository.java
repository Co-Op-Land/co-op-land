package com.coop.domain.player.repository;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.player.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByHistory(History history);
}
