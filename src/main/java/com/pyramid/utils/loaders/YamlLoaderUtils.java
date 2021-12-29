package com.pyramid.utils.loaders;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class YamlLoaderUtils {

  private static final YamlLoader yamlLoader = new YamlLoader();

  private YamlLoaderUtils() {
  }

  public static List loadDataFromYamlFileAsList(File yamlFile) {
    return loadDataFromYamlFile(yamlFile, List.class);
  }

  public static Map loadDataFromYamlFileAsMap(File yamlFile) {
    return loadDataFromYamlFile(yamlFile, Map.class);
  }

  private static <T> T loadDataFromYamlFile(File yamlFile, Class<T> type) {
    T data = null;
    try {
      data = yamlLoader.load(yamlFile, type);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }
}
