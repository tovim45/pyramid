package com.pyramid.infra.logger;

import static com.pyramid.utils.loaders.PropertiesLoader.saveProperties;

//import com.pyramid.infra.Context;
import com.pyramid.infra.ResourcesLocation;
import io.qameta.allure.Step;
import java.util.Objects;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AutomationLogger {

  private static final Logger LOGGER = LoggerFactory.getLogger(AutomationLogger.class);
  private static final String MESSAGE_STRUCTURE = "::: {}";
  private static final int REPORT_LOGGING_LEVEL = setReportLoggingLevel();
  private static final int DEFAULT_REPORT_LOGGING_LEVEL = 3;

  private AutomationLogger() {
  }

  /**
   * Set logging level for Allure Report based on "testReportLoggingLevel" command line parameter.
   * If "testReportLoggingLevel" parameter is not specified then the report logging level will be:
   * DEFAULT_REPORT_LOGGING_LEVEL.
   */
  private static int setReportLoggingLevel() {
    String testReportLoggingLevel = System.getProperty("testReportLoggingLevel");
    if (testReportLoggingLevel == null) {
      return DEFAULT_REPORT_LOGGING_LEVEL;
    }
    return switch (testReportLoggingLevel.toLowerCase()) {
      case "off" -> 0;
      case "error" -> 1;
      case "info" -> 2;
      case "debug" -> 3;
      case "trace" -> 4;
      default -> DEFAULT_REPORT_LOGGING_LEVEL;
    };
  }

  public static void setReportEnvironment() {
    Properties reportEnvironmentProperties = new Properties();
//    reportEnvironmentProperties.put("ENVIRONMENT", Context.getEnvironment());
    reportEnvironmentProperties.put("PROJECT",
        Objects.requireNonNullElse(System.getProperty("testProject"),
            "CLI parameter \"-DtestProject\" was not used."));
    reportEnvironmentProperties.put("SUITE",
        Objects.requireNonNullElse(System.getProperty("testSuite"),
            "CLI parameter \"-DtestSuite\" was not used."));
    saveProperties(reportEnvironmentProperties,
        ResourcesLocation.ALLURE_RESULTS_ENVIRONMENT_FILE.getPathAsFile(), "Allure Framework");
  }

  private static void printTraceToLog(String message) {
    LOGGER.trace(MESSAGE_STRUCTURE, message);
  }

  private static void printDebugToLog(String message) {
    LOGGER.debug(MESSAGE_STRUCTURE, message);
  }

  public static void printInfoToLog(String message) {
    LOGGER.info(MESSAGE_STRUCTURE, message);
  }

  private static void printErrorToLog(String message) {
    LOGGER.error(MESSAGE_STRUCTURE, message);
  }

  /**
   * Listener method for Allure Report. Method body not required.
   *
   * @param message to be displayed in Allure Report as: "[TRACE] ::: message"
   */
  @Step("[TRACE] ::: {0}")
  private static void printTraceToReport(String message) {
  }

  /**
   * Listener method for Allure Report. Method body not required.
   *
   * @param message to be displayed in Allure Report as: "[DEBUG] ::: message"
   */
  @Step("[DEBUG] ::: {0}")
  private static void printDebugToReport(String message) {
  }

  /**
   * Listener method for Allure Report. Method body not required.
   *
   * @param message to be displayed in Allure Report as: "[INFO] ::: message"
   */
  @Step("[INFO] ::: {0}")
  private static void printInfoToReport(String message) {
  }

  /**
   * Listener method for Allure Report. Method body not required.
   *
   * @param message to be displayed in Allure Report as: "[ERROR] ::: message"
   */
  @Step("[ERROR] ::: {0}")
  private static void printErrorToReport(String message) {
  }

  public static void trace(String message) {
    printTraceToLog(message);
    if (REPORT_LOGGING_LEVEL > 3) {
      printTraceToReport(message);
    }
  }

  public static void debug(String message) {
    printDebugToLog(message);
    if (REPORT_LOGGING_LEVEL > 2) {
      printDebugToReport(message);
    }
  }

  public static void info(String message) {
    printInfoToLog(message);
    if (REPORT_LOGGING_LEVEL > 1) {
      printInfoToReport(message);
    }
  }

  public static void error(String message) {
    printErrorToLog(message);
    if (REPORT_LOGGING_LEVEL > 0) {
      printErrorToReport(message);
    }
  }
}
