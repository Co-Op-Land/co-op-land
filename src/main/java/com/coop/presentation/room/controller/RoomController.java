package com.coop.presentation.room.controller;

import com.coop.domain.room.service.RoomService;
import com.coop.global.common.ApiResponse;
import com.coop.global.security.AuthUser;
import com.coop.presentation.room.dto.request.RoomCreateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateRequest;
import com.coop.presentation.room.dto.request.RoomUpdateStatusRequest;
import com.coop.presentation.room.dto.response.RoomReadDetailResponse;
import com.coop.presentation.room.dto.response.RoomReadResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid RoomCreateRequest request
    ) {
        Long roomId = roomService.generateRoom(authUser.memberId(), request);

        return ApiResponse.created(roomId);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomReadResponse>>> readRooms() {
        return ApiResponse.success(roomService.findRooms());
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<RoomReadDetailResponse>> readRooms(
            @PathVariable Long roomId
    ) {
        return ApiResponse.success(roomService.findRoom(roomId));
    }

    @PatchMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> updateRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId,
            @RequestBody @Valid RoomUpdateRequest request
    ) {
        roomService.modifyRoom(authUser.memberId(), roomId, request);

        return ApiResponse.noContent();
    }

    @PatchMapping("/{roomId}/status")
    public ResponseEntity<ApiResponse<Void>> updateRoomStatus(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId,
            @RequestBody @Valid RoomUpdateStatusRequest request
    ) {
        roomService.modifyRoomStatus(authUser.memberId(), roomId, request);

        return ApiResponse.noContent();
    }

    @PostMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Long>> joinRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId
    ) {
        roomService.joinRoom(authUser.memberId(), roomId);

        return ApiResponse.created(roomId);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Void>> leaveRoom(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long roomId
    ) {
        roomService.leaveRoom(authUser.memberId(), roomId);

        return ApiResponse.noContent();
    }
}
