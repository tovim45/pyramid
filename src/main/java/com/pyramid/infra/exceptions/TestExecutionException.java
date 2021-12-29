package com.pyramid.infra.exceptions;

public class TestExecutionException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public TestExecutionException(String errorMessage) {
    super(errorMessage);
  }
}
