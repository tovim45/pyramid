package com.pyramid.utils;

public class ThreadUtils {

  private ThreadUtils() {
  }

  public static void sleepXMillis(int timeoutInMillis) {
    try {
      Thread.sleep(timeoutInMillis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
  }

  public static void sleepXSeconds(int timeoutInSeconds) {
    sleepXMillis(timeoutInSeconds * 1000);
  }
}
