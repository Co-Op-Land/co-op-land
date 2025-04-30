package com.coop.domain.post.enums;

import com.coop.global.common.enums.ErrorCode;
import com.coop.global.exception.error.InvalidRequestException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PostCategory {
    WALKTHROUGH,
    RECOMMENDATION;

    public static PostCategory of(String category) {
        if (category == null) {
            return null;
        }

        return Arrays.stream(PostCategory.values())
                .filter(r -> r.name().equalsIgnoreCase(category))
                .findFirst()
                .orElseThrow(InvalidRequestException::new);
    }

    public static void isValid(String category) {
        try {
            PostCategory.valueOf(category.toUpperCase());
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(ErrorCode.INVALID_POST_CATEGORY);
        }
    }
}
