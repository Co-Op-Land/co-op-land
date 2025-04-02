package com.coop.domain.post.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PostCategory {
    WALKTHROUGH,
    RECOMMENDATION;

    public static PostCategory of(String status) {
        return Arrays.stream(PostCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("임시"));
    }
}
