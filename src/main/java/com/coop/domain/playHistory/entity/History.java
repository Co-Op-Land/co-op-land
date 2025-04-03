package com.coop.domain.playHistory.entity;

import com.coop.domain.room.entity.Room;
import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class History extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private Boolean isCompleted;

    @Builder
    public History(Room room, Boolean isCompleted) {
        this.room = room;
        this.isCompleted = isCompleted;
    }

    public void ProcessHistoryComplete() {
        this.isCompleted = true;
    }
}
