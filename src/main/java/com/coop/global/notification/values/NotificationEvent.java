package com.coop.global.notification.values;

import com.coop.domain.notification.enums.NotificationTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private NotificationTarget target;  //알림 유형
    private Long fromMemberId;  //발신자
    private ToMemberIds toMemberIds;    //수신자 리스트
    private Long relatedId; //연결된 pk(commentId, postId ...)
}
