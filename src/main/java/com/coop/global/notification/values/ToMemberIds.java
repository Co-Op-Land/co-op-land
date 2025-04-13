package com.coop.global.notification.values;

import com.coop.domain.notification.entity.Notification;
import lombok.Value;

import java.util.List;
import java.util.Objects;

@Value
public class ToMemberIds {

    List<Long> values;

    public ToMemberIds(List<Long> toMemberIds, Long fromMemberId) {
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
