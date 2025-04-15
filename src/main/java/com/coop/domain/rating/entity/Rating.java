package com.coop.domain.rating.entity;

import com.coop.domain.playHistory.entity.History;
import com.coop.domain.member.entity.Member;
import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "rating",
        uniqueConstraints = {@UniqueConstraint(
                columnNames = {"history_id, from_member_id, to_member_id"}
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

    @Builder
    public Rating(Long id, History history, Member fromMember, Member toMember, int score, String reason) {
        this.id = id;
        this.history = history;
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.score = score;
        this.reason = reason;
    }
}
