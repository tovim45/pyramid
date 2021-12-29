package com.pyramid.infra.logger;

import static com.pyramid.infra.logger.AutomationLogger.printInfoToLog;

import java.util.List;
import java.util.stream.Collectors;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

  private static final String SEPARATOR = "-------------------------------------------------------";
  private static final String TEST_STARTED_STRING = " ::: TEST STARTED ::: ";
  private static final String TEST_PASSED_STRING = " ::: TEST PASSED ::: ";
  private static final String TEST_FAILED_STRING = " ::: TEST FAILED ::: ";
  private static final String TEST_SKIPPED_STRING = " ::: TEST SKIPPED ::: ";

  @Override
  public void onTestStart(ITestResult iTestResult) {
    printInfoMessageToLog(iTestResult, TEST_STARTED_STRING);
  }

  @Override
  public void onTestSuccess(ITestResult iTestResult) {
    printInfoMessageToLog(iTestResult, TEST_PASSED_STRING);
  }

  @Override
  public void onTestFailure(ITestResult iTestResult) {
    printInfoMessageToLog(iTestResult,
        TEST_FAILED_STRING + "REASON: " + iTestResult.getThrowable());
  }

  @Override
  public void onTestSkipped(ITestResult iTestResult) {
    printInfoMessageToLog(iTestResult,
        TEST_SKIPPED_STRING + "REASON: " + getSkipCausedByList(iTestResult));

  }

  private void printInfoMessageToLog(ITestResult iTestResult, String message) {
    printInfoToLog(SEPARATOR);
    printInfoToLog(iTestResult.getMethod().getDescription() + message);
    printInfoToLog(SEPARATOR);
  }


  private List<String> getSkipCausedByList(ITestResult iTestResult) {
    return iTestResult.getSkipCausedBy().stream().map(ITestNGMethod::getQualifiedName)
        .collect(Collectors.toList());
  }
}
