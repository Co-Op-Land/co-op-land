package com.coop.global.notification.templates;

import com.coop.domain.post.entity.Post;
import com.coop.domain.post.repository.PostRepository;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.domain.notification.enums.NotificationTarget;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class CommentNotificationTemplate extends NotificationTemplate {

    private final PostRepository postRepository;

    public CommentNotificationTemplate(
            KafkaTemplate<String, NotificationEvent> kafkaTemplate,
            PostRepository postRepository
    ) {
        super(kafkaTemplate);
        this.postRepository = postRepository;
    }

    @Override
    public boolean supports(NotificationTarget target) {
        return target == NotificationTarget.COMMENT;
    }

    @Override
    protected NotificationEvent buildEvent(Object[] args, Object result, NotificationTarget target) {
        Long postId = (Long) args[1];
        User userDetails = (User) args[0];
        Post post = postRepository.findById(postId).orElseThrow();

        Long toMemberId = post.getMember().getId();
        Long fromMemberId = Long.valueOf(userDetails.getUsername());
        Long commentId = (result instanceof Long id) ? id : null;

        return NotificationEvent.builder()
                .target(NotificationTarget.COMMENT)
                .fromMemberId(fromMemberId)
                .toMemberId(toMemberId)
                .relatedId(commentId)
                .content("새 댓글이 달렸습니다.")
                .build();
    }
}
