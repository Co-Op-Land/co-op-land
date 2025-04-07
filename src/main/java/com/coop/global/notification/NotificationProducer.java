package com.coop.global.notification;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final PostRepository postRepository;

    @Around("@annotation(triggerNotification)")
    public Object notify(ProceedingJoinPoint joinPoint, TriggerNotification triggerNotification) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();

        Long toMemberId = extractToMemberId(args);
        Long fromMemberId = extractFromMemberId(args);
        Long relatedId = extractCommentId(result);

        if (toMemberId.equals(fromMemberId)) return result;

        NotificationEvent event = NotificationEvent.builder()
                .target(triggerNotification.target())
                .fromMemberId(fromMemberId)
                .toMemberId(toMemberId)
                .relatedId(relatedId)
                .content("새 댓글이 달렸습니다.")
                .build();

        kafkaTemplate.send("notification.comment", event);
        log.info("댓글 알림 전송: {}", event);

        return result;
    }

    private Long extractToMemberId(Object[] args) {
        Long postId = (Long) args[1];
        Post post = postRepository.findById(postId).orElse(null);
        return Objects.requireNonNull(post).getMember().getId();
    }

    private Long extractFromMemberId(Object[] args) {
        User userDetails = (User) args[0];
        return Long.valueOf(userDetails.getUsername());
    }

    private Long extractCommentId(Object result) {
        if (result instanceof Long id) return id;
        return null;
    }
}
