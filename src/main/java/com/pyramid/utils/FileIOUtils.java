package com.pyramid.utils;

import com.pyramid.infra.exceptions.FileIOUtilsException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;

public class FileIOUtils {

  private FileIOUtils() {
  }

  public static String readFileContentAsString(File file) {
    try {
      return Files.readString(file.toPath());
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: readFileContentAsString(File file) ::: Could not read file "
              + file.getPath());
    }
  }

  public static List<String> readFileContentLinesAsListOfString(File file) {
    return Arrays.asList(readFileContentAsString(file).split("\\R"));
  }

  public static void writeStringToFile(File file, String text) {
    try {
      Files.writeString(file.toPath(), text);
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: writeStringToFile(File file, String text) ::: Could not write to file "
              + file.getPath());
    }
  }

  public static void writeByteArrayToFile(byte[] byteArray, File outputFile) {
    try (var randomAccessFile = new RandomAccessFile(outputFile.getPath(), "rw")) {
      randomAccessFile.write(byteArray);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: writeByteArrayToFile(byte[] byteArray, File outputFile) ::: Could not find file "
              + outputFile.getPath());
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: writeByteArrayToFile(byte[] byteArray, File outputFile) ::: Could not write data to file "
              + outputFile.getPath());
    }
  }

  public static byte[] convertHexStringToByteArray(String hexString) {
    int hexStringLength = hexString.length();
    var byteArray = new byte[hexStringLength / 2];
    for (var i = 0; i < hexStringLength; i += 2) {
      byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
          + Character.digit(hexString.charAt(i + 1), 16));
    }
    return byteArray;
  }

  public static String convertByteArrayToHexString(byte[] byteArray) {
    var hexString = new StringBuilder();
    for (byte b : byteArray) {
      hexString.append(String.format("%02x", b));
    }
    return hexString.toString();
  }

  public static byte[] readFileToByteArray(File inputFile) {
    try {
      return Files.readAllBytes(inputFile.toPath());
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: readFileToByteArray(File inputFile) ::: Could not read data from file "
              + inputFile.getPath());
    }
  }

  public static void cleanDirectory(File directory) {
    try {
      FileUtils.cleanDirectory(directory);
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: cleanDirectory(File directory) ::: Could not remove contents from directory "
              + directory.getPath());
    }
  }

  public static void copyDirectory(File sourceDirectory, File destinationDirectory) {
    try {
      FileUtils.copyDirectory(sourceDirectory, destinationDirectory);
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: copyDirectory(File sourceDirectory, File destinationDirectory) ::: Could not copy directory "
              + sourceDirectory.getPath() + " to directory " + destinationDirectory.getPath());
    }
  }

  public static void deleteDirectory(File directory) {
    try {
      FileUtils.deleteDirectory(directory);
    } catch (IOException e) {
      e.printStackTrace();
      throw new FileIOUtilsException(
          "::: ERROR ::: deleteDirectory(File directory) ::: Could not delete directory "
              + directory.getPath());
    }
  }
}
