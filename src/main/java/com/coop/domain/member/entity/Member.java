package com.coop.domain.member.entity;

import com.coop.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private double rating;

    @Builder
    public Member(Role role, String email, String password, String nickname) {
        this.role = role != null ? role : Role.USER;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.rating = 0;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateRating(double rating) {
        this.rating = rating;
    }
}
