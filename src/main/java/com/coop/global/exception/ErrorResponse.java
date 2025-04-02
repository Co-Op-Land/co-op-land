package com.coop.global.exception;

import com.coop.global.common.enums.ErrorCode;

public record ErrorResponse(String message, String code) {

    public static ErrorResponse of(final ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getMessage(), errorCode.getCode());
    }

    public static ErrorResponse of(final ErrorCode errorCode, final String message) {
        return new ErrorResponse(message, errorCode.getCode());
    }
}