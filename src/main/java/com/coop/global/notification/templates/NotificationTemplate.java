package com.coop.global.notification.templates;

import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.repository.NotificationRepository;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.domain.notification.enums.NotificationTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Objects;

@RequiredArgsConstructor
public abstract class NotificationTemplate {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final NotificationRepository notificationRepository;

    public final void execute(Object[] args, Object result, NotificationTarget target) {
        NotificationEvent event = buildEvent(args, result, target);
        if (!validate(event)) return;
        save(event);
        publish(event);
    }

    protected boolean validate(NotificationEvent event) {
        return !Objects.equals(event.getToMemberId(), event.getFromMemberId());
    }

    protected void save(NotificationEvent event) {
        Notification notification = Notification.builder()
                .relatedId(event.getRelatedId())
                .target(event.getTarget())
                .fromMemberId(event.getFromMemberId())
                .toMemberId(event.getToMemberId())
                .build();
        notificationRepository.save(notification);
    }

    protected void publish(NotificationEvent event) {
        kafkaTemplate.send("notification." + event.getTarget().name().toLowerCase(), event);
    }

    public abstract boolean supports(NotificationTarget target);

    protected abstract NotificationEvent buildEvent(Object[] args, Object result, NotificationTarget target);
}
