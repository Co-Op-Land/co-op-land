package com.coop.presentation.game.controller;

import com.coop.domain.game.service.GameService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.game.dto.request.GameCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createGame(
            @RequestBody @Valid GameCreateRequest request
    ) {
        Long gameId = gameService.generateGame(request);

        return ApiResponse.created(gameId);
    }

    @DeleteMapping("/{gameId}")
    public ResponseEntity<ApiResponse<Void>> deleteGame(
            @PathVariable Long gameId
    ) {
        gameService.removeGame(gameId);

        return ApiResponse.noContent();
    }
}
