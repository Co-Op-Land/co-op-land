package com.coop.global.exception.error;

import com.coop.global.common.enums.ErrorCode;

public class EntityAlreadyExistException extends BaseException {
    public EntityAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EntityAlreadyExistException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public EntityAlreadyExistException() {
        super(ErrorCode.ENTITY_ALREADY_EXISTS);
    }
}
