package com.coop.presentation.matching.controller;

import com.coop.domain.matching.service.MatchingService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.matching.dto.request.MatchingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> joinMatching(
            @AuthenticationPrincipal User userDetails,
            @RequestBody MatchingRequest request
    ) {
        matchingService.joinMatching(Long.valueOf(userDetails.getUsername()), request);

        return ApiResponse.noContent();
    }
}
