package com.coop.presentation.admin;

import com.coop.domain.admin.AdminService;
import com.coop.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/rdb")
    public ResponseEntity<ApiResponse<String>> SendRDBNotification(
            @RequestBody AdminNotificationRequest requestDto,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        adminService.sendRDBNotification(memberId, requestDto);
        return ApiResponse.success("일괄 알림 전송 성공");
    }

    @PostMapping("/websocket")
    public ResponseEntity<ApiResponse<String>> SendWebSocketNotification(
            @RequestBody AdminNotificationRequest requestDto,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        adminService.sendWebSocketNotification(memberId, requestDto);
        return ApiResponse.success("일괄 실시간 알림 전송 성공");
    }
}
