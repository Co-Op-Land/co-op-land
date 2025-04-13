package com.coop.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification_recipient")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationRecipient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "notification_id")
    private Notification notification;

    @Column(nullable = false)
    private Long toMemberId;

    @Builder
    public NotificationRecipient(Notification notification, Long toMemberId) {
        this.notification = notification;
        this.toMemberId = toMemberId;
    }
}
