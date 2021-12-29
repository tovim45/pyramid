package com.pyramid.infra.exceptions;

public class DriverFactoryException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public DriverFactoryException(String errorMessage) {
    super(errorMessage);
  }
}
