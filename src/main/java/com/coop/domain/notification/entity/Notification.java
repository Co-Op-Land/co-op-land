package com.coop.domain.notification.entity;

import com.coop.global.common.BaseEntity;
import com.coop.domain.notification.enums.NotificationTarget;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long relatedId;

    @Enumerated(EnumType.STRING)
    private NotificationTarget target;

    private long fromMemberId;

    @Column(nullable = false)
    private Long toMemberId;

    @Column(nullable = false)
    private boolean isRead = false;

    @Builder
    public Notification(Long relatedId, NotificationTarget target, Long fromMemberId, Long toMemberId) {
        this.relatedId = relatedId;
        this.target = target;
        this.fromMemberId = fromMemberId;
        this.toMemberId = toMemberId;
    }
}
