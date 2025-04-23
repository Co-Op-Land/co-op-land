package com.coop.domain.notification.entity;

import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_recipient")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRecipient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(nullable = false)
    private Long toMemberId;

    @Column(nullable = false)
    private boolean isRead = false;

    @Builder
    public NotificationRecipient(Notification notification, Long toMemberId) {
        this.notification = notification;
        this.toMemberId = toMemberId;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
