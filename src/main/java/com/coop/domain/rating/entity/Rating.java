package com.coop.domain.rating.entity;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.member.entity.Member;
import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "rating",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"history, from_member, to_member"}
        )}
)
public class Rating extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "history_id")
    private History history;

    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Column(nullable = false)
    private int score;

    private String reason;
}
