package com.coop.global.notification.templates;

import com.coop.domain.notification.entity.Notification;
import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.domain.notification.repository.NotificationRepository;
import com.coop.domain.post.entity.Post;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.ToMemberIds;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentNotificationStrategy implements NotificationStrategy {

    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public boolean supports(NotificationTarget target) {
        return target == NotificationTarget.COMMENT;
    }

    @Override
    public NotificationEvent buildEvent(Object[] args, Object result) {
        Long postId = (Long) args[1];
        User userDetails = (User) args[0];
        Post post = postRepository.findById(postId).orElseThrow();

        Long toMemberId = post.getMember().getId();
        Long fromMemberId = Long.valueOf(userDetails.getUsername());
        Long commentId = (result instanceof Long id) ? id : null;

        return NotificationEvent.builder()
                .target(NotificationTarget.COMMENT)
                .fromMemberId(fromMemberId)
                .toMemberIds(new ToMemberIds(List.of(toMemberId), fromMemberId))
                .relatedId(commentId)
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