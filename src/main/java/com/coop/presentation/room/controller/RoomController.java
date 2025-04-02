package com.coop.presentation.room.controller;

import com.coop.domain.room.service.RoomService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.room.dto.request.RoomCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    public ResponseEntity<ApiResponse<Long>> createRoom(
            @AuthenticationPrincipal User userDetails,
            @RequestBody @Valid RoomCreateRequest request
    ) {
        Long roomId = roomService.generateRoom(Long.valueOf(userDetails.getUsername()), request);

        return ApiResponse.created(roomId);
    }
}
