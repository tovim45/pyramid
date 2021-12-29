package com.pyramid.tests;

import static com.pyramid.infra.logger.AutomationLogger.debug;
import static java.lang.System.getProperty;

import com.pyramid.infra.AppiumDriverSetup;
import com.pyramid.infra.SeleniumUtils;
import com.pyramid.infra.logger.AutomationLogger;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Attachment;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.xml.XmlTest;


public abstract class BaseTest {

  public static String envName;
  protected static ThreadLocal test = new ThreadLocal();
  protected SeleniumUtils seleniumUtils;
  public static HashMap<Long, WebDriver> webDrivers = new HashMap<>();
  public static Properties property;
  private XmlTest testngXml;
  protected AndroidDriver driver;
  public String apkDirectory = Paths.get(
      getProperty("user.dir"), "/src/main/resources/apk/").toString() + File.separator;
  public Path configDirectory;

  {
    try {
      configDirectory = Paths.get(System.getProperty("user.dir") + "/..").toRealPath();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Parameters({"env", "portal"})
  @BeforeSuite
  public void beforeSuite(String env, String portal) {
    envName = env;
    property = new Properties();
    try (FileReader fileReader = new FileReader(
        configDirectory + "/pyramid/src/main/java/com/pyramid/infra/config.properties")) {
      property.load(fileReader);
    } catch (IOException e) {
      e.printStackTrace();
    }
    debug("The Automation tests runs on url: " + property.getProperty(portal + "." + env));
    debug("user.dir = " + System.getProperty("user.dir"));
    debug("user.home = " + System.getProperty("user.home"));
    debug("Env: " + env);
    debug("Portal: " + portal);
  }

  @Parameters({"browserType", "isLinuxMachine"})
  @BeforeMethod
  public void startBrowser(ITestContext context, Method method, String browserType,
      String isLinuxMachine) throws MalformedURLException {
    debug("isLinuxMachine = " + isLinuxMachine);
    SeleniumUtils.isLinuxMachine = isLinuxMachine;
    String methodName = method.getName();
    String app;
    debug("method Name: " + methodName);
    if (method.getName().contains("UI")) {
      seleniumUtils = new SeleniumUtils();
      webDrivers.put(Thread.currentThread().getId(), seleniumUtils.getDriver(browserType));
    } else if (methodName.contains("Mobile")) {
      testngXml = context.getCurrentXmlTest();
      if (testngXml.getParameter("isJenkins").equalsIgnoreCase("true")) {
        app = apkDirectory + testngXml.getParameter("app");
      } else {
        app = "c:/apk/" + testngXml.getParameter("app");
      }
      debug(app);
      AppiumDriverSetup driverSetup = new AppiumDriverSetup();
      DesiredCapabilities dc = driverSetup.getCapabilities(testngXml.getAllParameters(), app);
      String serverUrl = testngXml.getParameter("serverUrl");
      debug("DesiredCapabilities " + dc);
      driver = new AndroidDriver<>(new URL(serverUrl + "/wd/hub"), dc);
      debug("Mobile Appium");
    } else if (method.getName().contains("DDT")) {
      debug("DDT tests started");
    }
  }

  @AfterMethod(alwaysRun = true, description = "Tear down: Test")
  public void afterMethod(ITestResult iTestResult, Method method) {
    if (method.getName().contains("UI")) {
      if (!iTestResult.isSuccess()) {
        seleniumUtils.analyzeLog();
        takeScreenshot();
      }
      seleniumUtils.closeRuntimeBrowserInstance();
    }
    }

    @AfterSuite(alwaysRun = true, description = "Tear down: Suite")
    public void afterSuite () {
      AutomationLogger.setReportEnvironment();
    }

    @Attachment(value = "SCREENSHOT", type = "image/png")
    private byte[] takeScreenshot () {
      return seleniumUtils.takeScreenshot();
    }
  }
