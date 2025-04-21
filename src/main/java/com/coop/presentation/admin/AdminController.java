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
    public ResponseEntity<ApiResponse<String>> SendRDBNotification(
            @RequestBody @Valid AdminNotificationRequest requestDto,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        adminService.sendRDBNotification(memberId, requestDto);
        return ApiResponse.success("전체 RDB 알림 전송 성공");
    }

    @PostMapping("/notification/websocket")
    public ResponseEntity<ApiResponse<String>> SendWebSocketNotification(
            @RequestBody @Valid AdminNotificationRequest requestDto,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        adminService.sendWebSocketNotification(memberId, requestDto);
        return ApiResponse.success("실시간 알림 전송 성공");
    }

    @GetMapping("/websocket")
    public ResponseEntity<ApiResponse<WebSocketIdListResponse>> getConnectedUserIds(){
        return ApiResponse.success(adminService.getConnectedUserIds());
    }
}
