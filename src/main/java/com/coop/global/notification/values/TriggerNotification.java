package com.coop.global.notification.values;

import com.coop.domain.notification.enums.NotificationTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TriggerNotification {
    NotificationTarget target();
}
