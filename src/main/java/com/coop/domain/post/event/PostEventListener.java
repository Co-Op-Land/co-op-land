package com.coop.domain.post.event;

import com.coop.domain.post.entity.PostDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Profile("rabbit")
@RequiredArgsConstructor
public class PostEventListener {

    private final RabbitTemplate rabbitTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePostSavedEvent(PostDocument event) {
        rabbitTemplate.convertAndSend("post.exchange", "post.saved", event);
    }
}
