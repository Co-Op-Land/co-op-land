package com.coop.domain.admin;

import com.coop.domain.member.entity.Member;
import com.coop.domain.member.enums.Role;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.AccessDeniedException;
import com.coop.global.notification.annotation.TriggerNotification;
import com.coop.global.websocket.WebSocketSessionManager;
import com.coop.presentation.admin.AdminNotificationRequest;
import com.coop.presentation.admin.WebSocketIdListResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberComponent memberComponent;
    private final WebSocketSessionManager webSocketSessionManager;

    @TriggerNotification(target = NotificationTarget.RDB)
    public void sendRDBNotification(Long memberId, AdminNotificationRequest requestDto) {
        validate(memberId);
        log.info("전체 공지사항 전송: 유저 - {}, 내용 - {}", memberId, requestDto.content());
    }

    @TriggerNotification(target = NotificationTarget.WEBSOCKET)
    public void sendWebSocketNotification(Long memberId, AdminNotificationRequest requestDto) {
        validate(memberId);
        log.info("실시간 공지사항 전송: 유저 - {}, 내용 - {}", memberId, requestDto.content());
    }

    public WebSocketIdListResponse getConnectedUserIds(){
        return WebSocketIdListResponse.from(webSocketSessionManager.getConnectedUserIdList());
    }

    private void validate(Long memberId) {
        Member member = memberComponent.findById(memberId);
        if (member.getRole() != Role.ADMIN) {
            throw new AccessDeniedException(ErrorCode.ACCESS_DENIED);
        }
    }
}
