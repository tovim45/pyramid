package com.pyramid.infra.exceptions;

public class ConfigLoaderException extends
    com.pyramid.infra.exceptions.CustomRuntimeException {

  public ConfigLoaderException(String errorMessage) {
    super(errorMessage);
  }
}
