package com.coop.global.exception.error;

import com.coop.global.common.enums.ErrorCode;

public class ForbiddenException extends BaseException {
  public ForbiddenException(ErrorCode errorCode) {
    super(errorCode);
  }

  public ForbiddenException() {
    super(ErrorCode.FORBIDDEN);
  }
}
