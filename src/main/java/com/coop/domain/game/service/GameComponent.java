package com.coop.domain.game.service;

import com.coop.domain.game.entity.Game;
import com.coop.domain.game.repository.GameRepository;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class GameComponent {

    private final GameRepository gameRepository;

    @Transactional(readOnly = true)
    public Game findGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.GAME_NOT_FOUND));
    }
}
