package com.coop.domain.player.repository;

import com.coop.domain.member.entity.QMember;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.entity.QHistory;
import com.coop.domain.player.entity.Player;
import com.coop.domain.player.entity.QPlayer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PlayerRepositoryCustomImpl implements PlayerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PlayerRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Map<History, List<Player>> findHistoriesWithPlayersByMemberId(Long memberId) {
        QPlayer player = QPlayer.player;
        QMember member = QMember.member;
        QHistory history = QHistory.history;

        List<Long> historyIds = findHistoriesByMember(memberId, player);
        if (historyIds.isEmpty()) {
            return Collections.emptyMap();
        }
        List<Player> players = findEveryPlayersFromHistories(player, member, history, historyIds);
        return classifyPlayersByHistory(players);
    }

    private static Map<History, List<Player>> classifyPlayersByHistory(List<Player> players) {
        Map<History, List<Player>> result = new HashMap<>();
        for (Player p : players) {
            result.computeIfAbsent(p.getHistory(), h -> new ArrayList<>()).add(p);
        }
        return result;
    }

    private List<Player> findEveryPlayersFromHistories(QPlayer player, QMember member, QHistory history, List<Long> historyIds) {
        List<Player> players = queryFactory
                .selectFrom(player)
                .join(player.member, member).fetchJoin()
                .join(player.history, history).fetchJoin()
                .where(player.history.id.in(historyIds))
                .fetch();
        return players;
    }

    private List<Long> findHistoriesByMember(Long memberId, QPlayer player) {
        return queryFactory
                .select(player.history.id)
                .from(player)
                .where(player.member.id.eq(memberId))
                .distinct()
                .fetch();
    }
}
