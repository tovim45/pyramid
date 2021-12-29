package com.pyramid.utils.loaders;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public class YamlLoader implements Serializable {

  private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory()).registerModule(
      new JavaTimeModule()).enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);

  public <T> T load(File configFile, Class<T> type) throws IOException {
    return MAPPER.readValue(configFile, type);
  }
}
