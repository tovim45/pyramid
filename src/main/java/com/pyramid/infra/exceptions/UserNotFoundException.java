package com.pyramid.infra.exceptions;

public class UserNotFoundException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public UserNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
