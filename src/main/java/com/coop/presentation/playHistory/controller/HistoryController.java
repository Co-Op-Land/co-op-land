package com.coop.presentation.playHistory.controller;

import com.coop.domain.playHistory.service.HistoryService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.playHistory.dto.response.HistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<List<HistoryResponse>>> readHistories(
            @PathVariable Long memberId
    ) {
        return ApiResponse.success(historyService.findMemberHistories(memberId));
    }
}
