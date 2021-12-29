package com.pyramid.utils;

import static com.pyramid.utils.loaders.YamlLoaderUtils.loadDataFromYamlFileAsMap;

import com.pyramid.infra.ResourcesLocation;
import java.io.File;
import java.util.Map;

public abstract class TestDataParser {

  private static final File testDataLocation = ResourcesLocation.TEST_DATA_DIR.getPathAsFile();

  private TestDataParser() {
  }

  public static Map<String, String> loadTestDataAsMapStringString(File testData) {
    return loadDataFromYamlFileAsMap(testData);
  }

  public static Map<String, Object> loadTestDataAsMapStringObject(File testData) {
    return loadDataFromYamlFileAsMap(testData);
  }
}
