package com.coop.global.notification.strategy;

import com.coop.domain.member.enums.Role;
import com.coop.domain.member.service.MemberComponent;
import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.notification.repository.NotificationRepository;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.ToMemberIds;
import com.coop.presentation.admin.AdminNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminRDBNotificationStrategy implements NotificationStrategy {

    private final NotificationRepository notificationRepository;
    private final MemberComponent memberComponent;
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
        Long fromMemberId = (Long) args[0];
        ToMemberIds toMemberIds = ToMemberIds.of(memberComponent.getMemberIdsByRole(Role.USER), fromMemberId);

        return NotificationEvent.from(
                NotificationTarget.RDB,
                fromMemberId,
                toMemberIds,
                requestDto.id(),
                requestDto.content(),
                requestDto.endPoint()
        );
    }

    @Override
    public void save(NotificationEvent event) {
        Notification notification = Notification.builder()
                .relatedId(event.relatedId())
                .target(event.target())
                .fromMemberId(event.fromMemberId())
                .content(event.content())
                .endPoint(event.endPoint())
                .build();
        event.toMemberIds().addTo(notification);
        notificationRepository.save(notification);
    }

    @Override
    public void publish(NotificationEvent event) {
        kafkaTemplate.send("notification." + event.target().name().toLowerCase(), event);
    }
}