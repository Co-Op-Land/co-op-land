package com.coop.global.notification.strategy;

import com.coop.domain.notification.enums.NotificationTarget;
import com.coop.global.notification.values.NotificationEvent;

public interface NotificationStrategy {

    //알림 유형을 bool 형으로 판단
    boolean supports(NotificationTarget target);

    //Kafka 에 전송할 event 객체 생성
    NotificationEvent buildEvent(Object[] args, Object result);

    //알림 예외처리: True 이면 Return
    boolean validate(NotificationEvent event);

    //RDB 에 알림 객체 저장(실시간 알림인 경우 오버라이딩만)
    void save(NotificationEvent event);

    //Kafka 에 Event 보냄
    void publish(NotificationEvent event);

    //전체 로직 정의
    default void execute(Object[] args, Object result) {
        NotificationEvent event = buildEvent(args, result);
        if (validate(event)) return;
        save(event);
        publish(event);
    }
}
