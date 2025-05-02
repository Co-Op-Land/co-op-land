package com.coop.global.exception.error;

import com.coop.global.common.enums.ErrorCode;

public class DistributedLockException extends BaseException {
  public DistributedLockException(ErrorCode errorCode) {
    super(errorCode);
  }

  public DistributedLockException() {
    super(ErrorCode.FORBIDDEN);
  }
}
