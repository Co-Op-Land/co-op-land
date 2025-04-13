package com.coop.global.notification.core;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.notification.templates.NotificationStrategy;
import com.coop.global.notification.values.TriggerNotification;
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

