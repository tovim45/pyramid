package com.pyramid.infra.exceptions;

public class ElementNotFoundException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public ElementNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
