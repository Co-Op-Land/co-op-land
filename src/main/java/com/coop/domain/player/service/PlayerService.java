package com.coop.domain.player.service;

import com.coop.domain.member.entity.Member;
import com.coop.domain.playHistory.entity.History;
import com.coop.domain.player.entity.Player;
import com.coop.domain.player.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Transactional
    public void generatePlayer(History history, List<Member> members) {
        for (Member member : members) {
            Player player = Player.builder()
                    .history(history)
                    .member(member)
                    .build();
            playerRepository.save(player);
        }
    }

    public Map<History, List<Player>> findPlayersFromHistoriesByMember(Long memberId) {
        return playerRepository.findHistoriesWithPlayersByMemberId(memberId);
    }
}
