package com.coop.global.notification.core;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import com.coop.global.notification.templates.NotificationTemplate;
import com.coop.global.notification.values.TriggerNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final List<NotificationTemplate> templates;

    @Around("@annotation(triggerNotification)")
    public Object notify(ProceedingJoinPoint joinPoint, TriggerNotification triggerNotification) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();

        NotificationTemplate template = templates.stream()
                .filter(t -> t.supports(triggerNotification.target()))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException(ErrorCode.INVALID_NOTIFICATION_TYPE));

        template.execute(args, result, triggerNotification.target());
        return result;
    }
}

