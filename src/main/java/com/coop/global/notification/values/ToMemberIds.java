package com.coop.global.notification.values;

import com.coop.domain.notification.entity.Notification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 수신자 List 를 편히 정의하기 위한 일급 컬렉션
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ToMemberIds {

    private final List<Long> values = new ArrayList<>();
    private Long fromMemberId;

    private ToMemberIds(List<Long> initial, Long fromMemberId) {
        this.fromMemberId = fromMemberId;
        initial.forEach(this::add);
    }

    public static ToMemberIds of(List<Long> toMemberIds, Long fromMemberId) {
        return new ToMemberIds(toMemberIds, fromMemberId);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    //List 에 add
    public void add(Long memberId) {
        if (memberId != null && !Objects.equals(memberId, fromMemberId) && !values.contains(memberId)) {
            values.add(memberId);
        }
    }

    //RDB 에 add
    public void addTo(Notification notification) {
        values.forEach(notification::addRecipient);
    }
}
