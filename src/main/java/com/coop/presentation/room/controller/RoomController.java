package com.coop.presentation.room.controller;

import com.coop.domain.room.enums.Status;
import com.coop.domain.room.service.RoomService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.room.dto.request.RoomCreateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createRoom(
            @AuthenticationPrincipal User userDetails,
            @RequestBody @Valid RoomCreateRequest request
    ) {
        Long roomId = roomService.generateRoom(Long.valueOf(userDetails.getUsername()), request);

        return ApiResponse.created(roomId);
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Long>> updateRoom(
            @AuthenticationPrincipal User userDetails,
            @PathVariable Long roomId,
            @RequestBody @Valid RoomUpdateRequest request
    ) {
        roomService.modifyRoom(Long.valueOf(userDetails.getUsername()), roomId, request);

        return ApiResponse.created(roomId);
    }
}
