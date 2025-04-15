package com.coop.domain.admin;

import com.coop.domain.member.enums.Role;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.global.notification.annotation.TriggerNotification;
import com.coop.global.websocket.WebSocketSessionManager;
import com.coop.presentation.admin.AdminNotificationRequest;
import com.coop.presentation.admin.AdminNotificationResponse;
import com.coop.presentation.admin.WebSocketIdListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final WebSocketSessionManager webSocketSessionManager;
    private final MemberComponent memberComponent;

    @TriggerNotification(target = NotificationTarget.RDB)
    public AdminNotificationResponse sendRDBNotification(Long memberId, AdminNotificationRequest requestDto) {
        log.info("전체 공지사항 전송: 유저 - {}, 내용 - {}", memberId, requestDto.content());
        return AdminNotificationResponse.from(getAllIds(), requestDto.content());
    }

    @TriggerNotification(target = NotificationTarget.WEBSOCKET)
    public AdminNotificationResponse sendWebSocketNotification(Long memberId, AdminNotificationRequest requestDto) {
        log.info("실시간 공지사항 전송: 유저 - {}, 내용 - {}", memberId, requestDto.content());
        return AdminNotificationResponse.from(getConnectedIds(), requestDto.content());
    }

    public WebSocketIdListResponse getConnectedUserIds(){
        return WebSocketIdListResponse.from(getConnectedIds());
    }

    private List<Long> getAllIds(){
        return memberComponent.getMemberIdsByRole(Role.USER);
    }

    private List<Long> getConnectedIds() {
        return webSocketSessionManager.getConnectedUserIdList();
    }
}
