package com.coop.global.notification.strategy;

import com.coop.domain.comment.entity.Comment;
import com.coop.domain.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    @Override
    public boolean supports(NotificationTarget target) {
        return target == NotificationTarget.COMMENT;
    }

    //알림 보내는 대상: 글 작성자, (대댓글이라면)부모 댓글 작성자
    //relatedId: postId
    @Override
    public NotificationEvent buildEvent(Object[] args, Object result) {
        Long postId = (Long) args[1];
        User userDetails = (User) args[0];
        Long commentId = (result instanceof Long id) ? id : null;
        Long fromMemberId = Long.valueOf(userDetails.getUsername());
        Post post = postRepository.findById(postId).orElseThrow();
        Long toPostWriterId = post.getMember().getId();
        ToMemberIds toMemberIds = ToMemberIds.of(List.of(toPostWriterId), fromMemberId);

        if (commentId != null) {
            Comment comment = commentRepository.findById(commentId).orElseThrow();
            if (comment.getParent() != null) {
                Long parentWriterId = comment.getParent().getMember().getId();
                toMemberIds.add(parentWriterId);
            }
        }
        return NotificationEvent.from(
                NotificationTarget.COMMENT,
                fromMemberId,
                toMemberIds,
                postId,
                "새로운 댓글이 등록되었습니다.",
                null
        );
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
