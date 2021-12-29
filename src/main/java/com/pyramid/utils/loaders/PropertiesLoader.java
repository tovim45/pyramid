package com.pyramid.utils.loaders;

import com.pyramid.infra.exceptions.PropertiesLoaderException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {

  private PropertiesLoader() {
  }

  public static Properties loadProperties(File file) {
    var properties = new Properties();
    try (var fis = new FileInputStream(file)) {
      properties.load(fis);
    } catch (FileNotFoundException e) {
      throw new PropertiesLoaderException(
          "::: ERROR ::: loadProperties(File file) ::: Could not find file: " + file.getPath());
    } catch (IOException e) {
      throw new PropertiesLoaderException(
          "::: ERROR ::: loadProperties(File file) ::: Could not load properties from file: "
              + file.getPath());
    }
    return properties;
  }

  public static void saveProperties(Properties properties, File file, String comments) {
    try (var fos = new FileOutputStream(file)) {
      properties.store(fos, comments);
    } catch (FileNotFoundException e) {
      throw new PropertiesLoaderException(
          "::: ERROR ::: saveProperties(Properties properties, File file, String comments) ::: Could not find file: "
              + file.getPath());
    } catch (IOException e) {
      throw new PropertiesLoaderException(
          "::: ERROR ::: saveProperties(Properties properties, File file, String comments) ::: Could not save properties to file: "
              + file.getPath());
    }
  }
}
