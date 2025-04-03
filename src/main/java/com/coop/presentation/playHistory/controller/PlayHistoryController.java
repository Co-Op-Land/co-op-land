package com.coop.presentation.playHistory.controller;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.playHistory.service.HistoryService;
import com.coop.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class PlayHistoryController {

    private final HistoryService historyService;

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse<History>> createHistory(
            @PathVariable Long id
    ) {
        History history = historyService.generateHistory(id);
        return ApiResponse.created(history);
    }

    @GetMapping
    public void readHistory(
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        
    }
}
