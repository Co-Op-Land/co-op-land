package com.coop.global.exception.error;

import com.coop.global.common.enums.ErrorCode;

public class ElasticsearchException extends BaseException {
    public ElasticsearchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
