package com.coop.domain.game.service;

import com.coop.domain.game.entity.Game;
import com.coop.domain.game.repository.GameRepository;
import com.coop.presentation.game.dto.request.GameCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameComponent gameComponent;
    private final GameRepository gameRepository;

    @Transactional
    public Long generateGame(GameCreateRequest request) {
        Game game = gameRepository.save(request.toEntity());

        return game.getId();
    }

    @Transactional
    public void removeGame(Long gameId) {
        Game game = gameComponent.findGameById(gameId);

        gameRepository.deleteById(game.getId());
    }
}
