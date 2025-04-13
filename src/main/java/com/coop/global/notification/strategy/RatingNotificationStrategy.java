package com.coop.global.notification.strategy;

import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.notification.repository.NotificationRepository;
import com.coop.domain.rating.entity.Rating;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.ToMemberIds;
import com.coop.presentation.rating.dto.request.RatingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RatingNotificationStrategy implements NotificationStrategy {

    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public boolean supports(NotificationTarget target) {
        return target == NotificationTarget.RATING;
    }

    //알림 보내는 대상: 평가 대상자
    @Override
    public NotificationEvent buildEvent(Object[] args, Object result) {
        RatingRequest request = (RatingRequest) args[0];
        Long ratingId = (result instanceof Rating r) ? r.getId() : null;
        Long fromMemberId = (Long) args[1];
        Long toMemberId = request.toMemberId();
        ToMemberIds toMemberIds = ToMemberIds.of(List.of(toMemberId), fromMemberId);

        return NotificationEvent.builder()
                .target(NotificationTarget.RATING)
                .fromMemberId(fromMemberId)
                .toMemberIds(toMemberIds)
                .relatedId(ratingId)
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
                .build();
        event.getToMemberIds().addTo(notification);
        notificationRepository.save(notification);
    }

    @Override
    public void publish(NotificationEvent event) {
        kafkaTemplate.send("notification." + event.getTarget().name().toLowerCase(), event);
    }
}
