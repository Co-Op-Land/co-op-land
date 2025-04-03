package com.coop.domain.room.entity;

import com.coop.domain.game.entity.Game;
import com.coop.domain.member.entity.Member;
import com.coop.domain.room.enums.Difficulty;
import com.coop.domain.room.enums.Status;
import com.coop.domain.room.enums.Visibility;
import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private Integer maxPlayerCount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id")
    private Member host;

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
            Visibility visibility,
            Integer maxPlayerCount
    ) {
        this.game = game;
        this.host = member;
        this.title = title;
        this.difficulty = difficulty;
        this.status = status;
        this.visibility = visibility;
        this.maxPlayerCount = maxPlayerCount;
    }

    public void update(String title, Integer maxPlayerCount, Difficulty difficulty, Visibility visibility) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }

        if (maxPlayerCount != null) {
            this.maxPlayerCount = maxPlayerCount;
        }

        if (difficulty != null) {
            this.difficulty = difficulty;
        }

        if (visibility != null) {
            this.visibility = visibility;
        }
    }

    public void updateStatus(Status status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void close() {
        this.status = Status.CLOSED;
    }
}
