package com.coop.global.notification.values;

import com.coop.domain.notification.enums.NotificationTarget;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record NotificationEvent(    //kafka 용 이벤트 객체
        NotificationTarget target,  //알림 유형
        Long fromMemberId,  //발신자
        ToMemberIds toMemberIds,    //수신자 리스트
        Long relatedId, //연결된 pk(commentId, postId ...)
        String content,
        String endPoint    //관리자 RDB 알림의 경우 리디렉션 위해 엔드포인트 필요
) {
    public static NotificationEvent from(
            NotificationTarget target,
            Long fromMemberId,
            ToMemberIds toMemberIds,
            Long relatedId,
            String content,
            String endPoint
    ) {
        return NotificationEvent.builder()
                .target(target)
                .fromMemberId(fromMemberId)
                .toMemberIds(toMemberIds)
                .relatedId(relatedId)
                .content(content)
                .endPoint(endPoint)
                .build();
    }
}

//TODO: Event 객체와 실제 유저에게 전송되는 메세지 분리