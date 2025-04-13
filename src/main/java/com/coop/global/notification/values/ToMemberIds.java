package com.coop.global.notification.values;

import com.coop.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToMemberIds {

    private List<Long> values;

    public ToMemberIds(List<Long> toMemberIds, Long fromMemberId) {
        this.values = toMemberIds.stream()
                .filter(id -> !Objects.equals(id, fromMemberId))
                .distinct()
                .toList();
    }

    public void addTo(Notification notification) {
        if (values != null) {
            values.forEach(notification::addRecipient);
        }
    }

    public boolean isEmpty() {
        return values == null || values.isEmpty();
    }
}
