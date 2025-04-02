package com.coop.global.exception.error;

import com.coop.global.common.enums.ErrorCode;

public class UnAuthorizedException extends BaseException {
    public UnAuthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}