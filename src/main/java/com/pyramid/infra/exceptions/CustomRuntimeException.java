package com.pyramid.infra.exceptions;

import static com.pyramid.infra.logger.AutomationLogger.error;

public class CustomRuntimeException extends RuntimeException {

  protected CustomRuntimeException(String errorMessage) {
    super(errorMessage);
    error(errorMessage);
  }
}
