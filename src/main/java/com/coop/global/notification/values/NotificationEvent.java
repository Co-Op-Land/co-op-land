package com.coop.global.notification.values;

import com.coop.domain.notification.enums.NotificationTarget;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationEvent {
    private NotificationTarget target;  //알림 유형
    private Long fromMemberId;  //발신자
    private ToMemberIds toMemberIds;    //수신자 리스트
    private Long relatedId; //연결된 pk(commentId, postId ...)
    private String content;
    private String endPoint;    //관리자 RDB 알림의 경우 리디렉션 위해 엔드포인트 필요
}

//TODO: Event 객체와 실제 유저에게 전송되는 메세지 분리