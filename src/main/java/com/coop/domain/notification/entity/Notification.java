package com.coop.domain.notification.entity;

import com.coop.domain.notification.enums.NotificationTarget;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notification")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long relatedId;

    @Enumerated(EnumType.STRING)
    private NotificationTarget target;

    private long fromMemberId;

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationRecipient> toMemberIds = new ArrayList<>();

    private String content;

    private String endPoint;

    @Builder
    public Notification(long relatedId, NotificationTarget target, long fromMemberId, String content, String endPoint) {
        this.relatedId = relatedId;
        this.target = target;
        this.fromMemberId = fromMemberId;
        this.content = content;
        this.endPoint = endPoint;
    }

    public void addRecipient(Long toMemberId) {
        NotificationRecipient recipient = NotificationRecipient.builder()
                .notification(this)
                .toMemberId(toMemberId)
                .build();
        this.toMemberIds.add(recipient);
    }
}
