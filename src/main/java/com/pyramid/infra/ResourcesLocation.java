package com.pyramid.infra;

import java.io.File;

public enum ResourcesLocation {
  MODULE_LOCATORS_DIR("src/test/resources/module-locators"),
  GENERAL_CONFIG_FILE("src/test/resources/general/general_config.yaml"),
  CREDENTIALS_FILE("src/test/resources/general/credentials.yaml"),
  TEST_DATA_DIR("src/test/resources/test-data"),
  TEMP_DIR("src/test/resources/temp"),
  DATA_SERVER_CONFIG_FILE("src/test/resources/general/data_server_config.yaml"),
  SAMPLE_FILES_DIR("src/test/resources/test-data/sample-files"),
  RESOURCES_LOADER_FILE("src/main/resources/configs/resources_loader.properties"),
  COMMON_ENTITIES_TEST_DATA_FILE(
      "src/test/resources/test-data/sanity/priority1/common_entities_test_data.yaml"),
  ALLURE_RESULTS_ENVIRONMENT_FILE("target/allure-results/environment.properties");

  private final String path;

  ResourcesLocation(String path) {
    this.path = path;
  }

  public File getPathAsFile() {
    return new File(path);
  }

  public String getPathAsString() {
    return path;
  }
}
