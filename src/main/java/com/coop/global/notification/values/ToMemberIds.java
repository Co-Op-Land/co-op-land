package com.coop.global.notification.values;

import com.coop.domain.notification.entity.Notification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToMemberIds {

    private final List<Long> values = new ArrayList<>();

    private ToMemberIds(List<Long> initial, Long fromMemberId) {
        initial.stream()
                .filter(id -> !Objects.equals(id, fromMemberId))
                .distinct()
                .forEach(this::add);
    }

    public static ToMemberIds of(List<Long> toMemberIds, Long fromMemberId) {
        return new ToMemberIds(toMemberIds, fromMemberId);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void addTo(Notification notification) {
        values.forEach(notification::addRecipient);
    }

    public void add(Long memberId) {
        if (memberId != null && !values.contains(memberId)) {
            values.add(memberId);
        }
    }
}
