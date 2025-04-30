package com.coop.global.notification.values;

import com.coop.domain.notification.entity.Notification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

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

    //현재 웹소켓 세션이 있는 유저만 필터링
    public ToMemberIds filterConnected(List<Long> connectedUserIds) {
        Set<Long> connectedSet = new HashSet<>(connectedUserIds);
        List<Long> filtered = this.values.stream()
                .filter(connectedSet::contains)
                .toList();
        if (values.isEmpty() || connectedUserIds.isEmpty()) {
            return ToMemberIds.of(List.of(), this.fromMemberId);
        }
        return ToMemberIds.of(filtered, this.fromMemberId);
    }

}
