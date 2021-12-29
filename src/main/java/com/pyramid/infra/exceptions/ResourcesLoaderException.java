package com.pyramid.infra.exceptions;

public class ResourcesLoaderException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public ResourcesLoaderException(String errorMessage) {
    super(errorMessage);
  }
}
