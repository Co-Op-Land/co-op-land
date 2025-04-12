package com.coop.global.notification.core;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.TriggerNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final List<NotificationStrategy> strategies;

    @Around("@annotation(triggerNotification)")
    public Object notify(ProceedingJoinPoint joinPoint, TriggerNotification triggerNotification) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();

        NotificationStrategy strategy = strategies.stream()
                .filter(s -> s.supports(triggerNotification.target()))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(ErrorCode.INVALID_NOTIFICATION_TYPE));

        NotificationEvent event = strategy.buildEvent(args, result);

        //자기 자신에게 보내는 알림은 무시
        if (Objects.equals(event.getToMemberId(), event.getFromMemberId())) return result;

        kafkaTemplate.send("notification." + event.getTarget().name().toLowerCase(), event);
        log.info("알림 전송: {}", event);
        return result;
    }
}

