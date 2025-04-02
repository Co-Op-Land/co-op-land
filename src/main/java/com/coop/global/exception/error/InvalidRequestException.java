package com.coop.global.exception.error;

import com.coop.global.common.enums.ErrorCode;

public class InvalidRequestException extends BaseException {
  public InvalidRequestException(ErrorCode errorCode) {
    super(errorCode);
  }

  public InvalidRequestException() {
    super(ErrorCode.INVALID_REQUEST);
  }
}
