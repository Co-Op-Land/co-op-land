package com.coop.global.notification.templates;

import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.global.notification.values.NotificationEvent;

public interface NotificationStrategy {

    boolean supports(NotificationTarget target);

    NotificationEvent buildEvent(Object[] args, Object result);

    boolean validate(NotificationEvent event);

    void save(NotificationEvent event);

    void publish(NotificationEvent event);

    void execute(Object[] args, Object result);
}