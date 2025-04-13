package com.coop.domain.game.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer maxPlayerCounts;
    private String name;

    @Builder
    public Game(
            Integer maxPlayerCounts,
            String name
    ) {
        this.maxPlayerCounts = maxPlayerCounts;
        this.name = name;
    }
}
