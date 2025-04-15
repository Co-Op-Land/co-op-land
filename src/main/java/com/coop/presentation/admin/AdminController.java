package com.coop.presentation.admin;

import com.coop.domain.admin.AdminService;
import com.coop.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/notification/rdb")
    public ResponseEntity<ApiResponse<AdminNotificationResponse>> SendRDBNotification(
            @RequestBody @Valid AdminNotificationRequest requestDto,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        AdminNotificationResponse responseDto = adminService.sendRDBNotification(memberId, requestDto);
        return ApiResponse.success(responseDto);
    }

    @PostMapping("/notification/websocket")
    public ResponseEntity<ApiResponse<AdminNotificationResponse>> SendWebSocketNotification(
            @RequestBody @Valid AdminNotificationRequest requestDto,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        AdminNotificationResponse responseDto = adminService.sendWebSocketNotification(memberId, requestDto);
        return ApiResponse.success(responseDto);
    }

    @GetMapping("/websocket")
    public ResponseEntity<ApiResponse<WebSocketIdListResponse>> getConnectedUserIds(){
        return ApiResponse.success(adminService.getConnectedUserIds());
    }
}
