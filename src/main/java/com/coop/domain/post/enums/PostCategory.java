package com.coop.domain.post.enums;

import com.coop.global.exception.error.InvalidRequestException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PostCategory {
    WALKTHROUGH,
    RECOMMENDATION;

    public static PostCategory of(String status) {
        if (status == null) {
            return null;
        }

        return Arrays.stream(PostCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(InvalidRequestException::new);
    }
}
