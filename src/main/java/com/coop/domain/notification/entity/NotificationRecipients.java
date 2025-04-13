package com.coop.domain.notification.entity;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class NotificationRecipients {

    private final List<Long> values;

    public NotificationRecipients(List<Long> toMemberIds, Long fromMemberId) {
        this.values = toMemberIds.stream()
                .filter(id -> !Objects.equals(id, fromMemberId))
                .distinct()
                .toList();
    }

    public void addTo(Notification notification) {
        values.forEach(notification::addRecipient);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
