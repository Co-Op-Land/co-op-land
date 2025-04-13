package com.coop.domain.notification.entity;

import com.coop.domain.member.entity.Member;
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

    private Long related_id;

    @Enumerated(EnumType.STRING)
    private NotificationTarget target;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    private boolean isRead = false;

    @Builder
    public Notification(Long related_id, NotificationTarget target, Member fromMember, Member toMember) {
        this.related_id = related_id;
        this.target = target;
        this.fromMember = fromMember;
        this.toMember = toMember;
    }
}
