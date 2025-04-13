package com.coop.presentation.notification.controller;

import com.coop.domain.notification.service.NotificationService;
import com.coop.global.common.ApiResponse;
import com.coop.presentation.notification.dto.response.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{notificationId}")
    public ResponseEntity<ApiResponse<NotificationResponse>> readNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        NotificationResponse responseDto = notificationService.readNotification(memberId, notificationId);
        return ApiResponse.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> readAllNotifications(
            @AuthenticationPrincipal User userDetails
    ) {
        Long memberId = Long.valueOf(userDetails.getUsername());
        List<NotificationResponse> responseDto = notificationService.readAllNotifications(memberId);
        return ApiResponse.success(responseDto);
    }
}
