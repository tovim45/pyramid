package com.pyramid.infra.exceptions;

public class ModuleFactoryException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public ModuleFactoryException(String errorMessage) {
    super(errorMessage);
  }
}
