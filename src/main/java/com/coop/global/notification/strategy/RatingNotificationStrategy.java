package com.coop.global.notification.strategy;

import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.notification.repository.NotificationRepository;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.ToMemberIds;
import com.coop.presentation.rating.dto.request.RatingRequest;
import com.coop.presentation.rating.dto.response.RatingResponse;
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
    //relatedId: ratingId
    @Override
    public NotificationEvent buildEvent(Object[] args, Object result) {
        RatingRequest request = (RatingRequest) args[0];
        Long ratingId = (result instanceof RatingResponse r) ? r.id() : null;
        Long fromMemberId = (Long) args[1];
        Long toMemberId = request.toMemberId();
        ToMemberIds toMemberIds = ToMemberIds.of(List.of(toMemberId), fromMemberId);

        return NotificationEvent.from(
                NotificationTarget.RATING,
                fromMemberId,
                toMemberIds,
                ratingId,
                "새로운 평점이 등록되었습니다.",
                null
        );
    }

    @Override
    public boolean validate(NotificationEvent event) {
        return event.toMemberIds().isEmpty();
    }

    @Override
    public void save(NotificationEvent event) {
        Notification notification = Notification.builder()
                .relatedId(event.relatedId())
                .target(event.target())
                .fromMemberId(event.fromMemberId())
                .content(event.content())
                .build();
        event.toMemberIds().addTo(notification);
        notificationRepository.save(notification);
    }

    @Override
    public void publish(NotificationEvent event) {
        kafkaTemplate.send("notification." + event.target().name().toLowerCase(), event);
    }
}
