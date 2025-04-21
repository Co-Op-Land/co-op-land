package com.coop.presentation.admin;

import com.coop.domain.admin.AdminService;
import com.coop.domain.member.enums.Role;
import com.coop.global.common.ApiResponse;
import com.coop.global.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Secured(Role.Authority.ADMIN)
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/notification/rdb")
    public ResponseEntity<ApiResponse<String>> SendRDBNotification(
            @RequestBody @Valid AdminNotificationRequest requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        adminService.sendRDBNotification(authUser.memberId(), requestDto);
        return ApiResponse.success("전체 RDB 알림 전송 성공");
    }

    @PostMapping("/notification/websocket")
    public ResponseEntity<ApiResponse<String>> SendWebSocketNotification(
            @RequestBody @Valid AdminNotificationRequest requestDto,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        adminService.sendWebSocketNotification(authUser.memberId(), requestDto);
        return ApiResponse.success("실시간 알림 전송 성공");
    }

    @GetMapping("/websocket")
    public ResponseEntity<ApiResponse<WebSocketIdListResponse>> getConnectedUserIds(){
        return ApiResponse.success(adminService.getConnectedUserIds());
    }
}
