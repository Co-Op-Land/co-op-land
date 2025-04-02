package com.coop.domain.room.entity;

import com.coop.domain.game.entity.Game;
import com.coop.domain.member.entity.Member;
import com.coop.domain.room.enums.Difficulty;
import com.coop.domain.room.enums.Status;
import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "rooms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int maxPlayerCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime playingStartedAt;

    @Builder
    public Room(
            Game game,
            Member member,
            String title,
            Difficulty difficulty,
            Status status,
            int maxPlayerCount
    ) {
        this.game = game;
        this.member = member;
        this.title = title;
        this.difficulty = difficulty;
        this.status = status;
        this.maxPlayerCount = maxPlayerCount;
    }

    public void update(String title, Difficulty difficulty, Integer maxPlayerCount) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }

        if (difficulty != null) {
            this.difficulty = difficulty;
        }

        if (maxPlayerCount != null) {
            this.maxPlayerCount = maxPlayerCount;
        }
    }
}
