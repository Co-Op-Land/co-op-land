package com.coop.global.notification.core;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.notification.templates.NotificationStrategy;
import com.coop.global.notification.annotation.TriggerNotification;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final List<NotificationStrategy> strategies;

    /**
     * 어노테이션이 달린 메서드에서 알림 객체를 추출하고, Kafka 로 전송하는 메서드
     * @param joinPoint 해당 메서드
     * @param triggerNotification 어노테이션(알림 유형 추출용)
     */
    @Around("@annotation(triggerNotification)")
    public Object notify(ProceedingJoinPoint joinPoint, TriggerNotification triggerNotification) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();

        NotificationStrategy strategy = strategies.stream()
                .filter(s -> s.supports(triggerNotification.target()))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(ErrorCode.INVALID_NOTIFICATION_TYPE));

        strategy.execute(args, result);
        return result;
    }
}
