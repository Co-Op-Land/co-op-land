package com.coop.global.notification.strategy;

import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.ToMemberIds;
import com.coop.presentation.admin.AdminNotificationRequest;
import com.coop.presentation.admin.AdminNotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AdminWebSocketNotificationStrategy implements NotificationStrategy {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public boolean supports(NotificationTarget target) {
        return target == NotificationTarget.WEBSOCKET;
    }

    //알림 보내는 대상: 현재 접속중인 모든 유저
    //relatedId: 관리자 지정 id
    @Override
    public NotificationEvent buildEvent(Object[] args, Object result) {
        AdminNotificationRequest requestDto = (AdminNotificationRequest) args[1];
        Long fromMemberId = (Long) args[0];
        AdminNotificationResponse responseDto = (result instanceof AdminNotificationResponse dto) ? dto : null;
        ToMemberIds toMemberIds = ToMemberIds.of(Objects.requireNonNull(responseDto).ids(), fromMemberId);

        return NotificationEvent.from(
                NotificationTarget.WEBSOCKET,
                fromMemberId,
                toMemberIds,
                requestDto.id(),
                requestDto.content(),
                requestDto.endPoint()
        );
    }

    @Override
    public void save(NotificationEvent event) {
        //실시간 공지사항은 저장하지 않음
    }

    @Override
    public void publish(NotificationEvent event) {
        kafkaTemplate.send("notification." + event.target().name().toLowerCase(), event);
    }
}