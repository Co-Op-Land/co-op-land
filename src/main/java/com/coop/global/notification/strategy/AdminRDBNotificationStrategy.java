package com.coop.global.notification.strategy;

import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.notification.repository.NotificationRepository;
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
public class AdminRDBNotificationStrategy implements NotificationStrategy {

    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public boolean supports(NotificationTarget target) {
        return target == NotificationTarget.RDB;
    }

    //알림 보내는 대상: 모든 유저
    //relatedId: 관리자 지정 id
    @Override
    public NotificationEvent buildEvent(Object[] args, Object result) {
        AdminNotificationRequest requestDto = (AdminNotificationRequest) args[1];
        AdminNotificationResponse responseDto = (result instanceof AdminNotificationResponse dto) ? dto : null;
        Long fromMemberId = (Long) args[0];
        ToMemberIds toMemberIds = ToMemberIds.of(Objects.requireNonNull(responseDto).ids(), fromMemberId);

        return NotificationEvent.builder()
                .target(NotificationTarget.RDB)
                .fromMemberId(fromMemberId)
                .toMemberIds(toMemberIds)
                .relatedId(requestDto.id())
                .content(requestDto.content())
                .endPoint(requestDto.endPoint())
                .build();
    }

    @Override
    public boolean validate(NotificationEvent event) {
        return event.getToMemberIds().isEmpty();
    }

    @Override
    public void save(NotificationEvent event) {
        Notification notification = Notification.builder()
                .relatedId(event.getRelatedId())
                .target(event.getTarget())
                .fromMemberId(event.getFromMemberId())
                .content(event.getContent())
                .endPoint(event.getEndPoint())
                .build();
        event.getToMemberIds().addTo(notification);
        notificationRepository.save(notification);
    }

    @Override
    public void publish(NotificationEvent event) {
        kafkaTemplate.send("notification." + event.getTarget().name().toLowerCase(), event);
    }
}