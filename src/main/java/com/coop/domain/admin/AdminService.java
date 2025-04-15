package com.coop.domain.admin;

import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.global.notification.annotation.TriggerNotification;
import com.coop.presentation.admin.AdminNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    @TriggerNotification(target = NotificationTarget.RDB)
    public void sendRDBNotification(Long memberId, AdminNotificationRequest requestDto) {
    }

    @TriggerNotification(target = NotificationTarget.WEBSOCKET)
    public void sendWebSocketNotification(Long memberId, AdminNotificationRequest requestDto) {
    }
}
