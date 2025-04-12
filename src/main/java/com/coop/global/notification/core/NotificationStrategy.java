package com.coop.global.notification.core;

import com.coop.global.notification.values.NotificationEvent;
import com.coop.global.notification.values.NotificationTarget;

public interface NotificationStrategy {

    boolean supports(NotificationTarget target);
    NotificationEvent buildEvent(Object[] args, Object result);
}
