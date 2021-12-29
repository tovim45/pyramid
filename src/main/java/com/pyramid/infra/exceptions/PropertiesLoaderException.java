package com.pyramid.infra.exceptions;

public class PropertiesLoaderException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public PropertiesLoaderException(String errorMessage) {
    super(errorMessage);
  }
}
