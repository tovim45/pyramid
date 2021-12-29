package com.pyramid.infra.exceptions;

public class TestsExecutionContextException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public TestsExecutionContextException(String errorMessage) {
    super(errorMessage);
  }
}
