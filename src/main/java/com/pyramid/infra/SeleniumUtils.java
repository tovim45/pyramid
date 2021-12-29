package com.pyramid.infra;

import static com.pyramid.infra.logger.AutomationLogger.debug;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;

/**
 * This class contains convenience methods for working with Selenium. Giora Tovim
 */
public class SeleniumUtils {

  private WebDriver driver;
  public static String reportDirectory;
  public static String isLinuxMachine;
  private String downloadDir = java.nio.file.Paths.get(
      System.getProperty("user.dir"), "src", "test", "resources", "downloads").toString()
      + File.separator;

  /**
   * Reads General Parameters from application.properties Sets browser (Chrome, Firefox, IE etc...)
   * and navigates to the class related page
   */
  public WebDriver getDriver(String browserType) {
    driver = setBrowser(browserType);
    if (isLinuxMachine.equalsIgnoreCase("FALSE")) {
      driver.manage().window().maximize();
    }
    driver.manage().deleteAllCookies();
    return driver;
  }

  public void closeRuntimeBrowserInstance() {
    if (driver != null) {
      driver.quit();
    }
  }

  /**
   * This method set browser BrowserType: Chrome - CHROME FireFox - FF Internet Explorer - IE
   *
   * @param browserType CHROME FF IE
   * @return driver
   * @author Giora Tovim
   */
  public WebDriver setBrowser(String browserType) {
    debug("Web browser switch: " + browserType);
    switch (browserType) {
      case "FF" -> {
        debug("Starting FireFox web driver");
        System.setProperty("webdriver.gecko.driver", pathToDrivers("geckodriver.exe"));
        driver = new FirefoxDriver();
        debug("FireFox web driver started successfully");
      }
      case "CHROME" -> {
        debug("Starting CHROME web driver");
        debug(new File(System.getProperty("user.dir")).getParent());
        if (!isLinuxMachine.contains("FALSE")) {
          System.setProperty("webdriver.chrome.driver", pathToDrivers("chromedriver"));
          debug("set System setProperty for chromedriver Linux");
        } else {
          System.setProperty("webdriver.chrome.driver", pathToDrivers("chromedriver.exe"));
          debug("set System setProperty for chromedriver Windows");
        }
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadDir);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("test-type");
        options.addArguments("--incognito");
        options.addArguments("--no-sandbox");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-setuid-sandbox");
        options.setExperimentalOption("prefs", chromePrefs);
        if (!isLinuxMachine.equalsIgnoreCase("FALSE")) {
          options.setBinary("/usr/bin/google-chrome");
          options.addArguments("--headless"); //!!!should be enabled for Jenkins
          options.addArguments("--window-size=1920,1080");
          options.addArguments("--disable-extensions"); // disabling extensions
          options.addArguments("--disable-gpu"); // applicable to windows os only
          options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
          options.addArguments("--no-sandbox"); // Bypass OS security model
          options.setExperimentalOption("w3c", true);
          options.addArguments("--allowed-ips");
        }
        driver = new ChromeDriver(options);
        debug("CHROME web driver started successfully");
      }
      case "EDGE" -> {
        debug("Starting EDGE web driver");
        System.setProperty("webdriver.edge.driver", pathToDrivers("MicrosoftWebDriver.exe"));
        driver = new EdgeDriver();
        debug("EDGE web driver started successfully");
      }
      case "IE" -> {
        debug("Starting IE web driver");
        System.setProperty("webdriver.ie.driver", pathToDrivers("IEDriverServer.exe"));
        driver = new InternetExplorerDriver();
        debug("IE web driver started successfully");
      }
      default -> debug("Browser name is not exist!!!");
    }
    return driver;
  }

  /**
   * This method get screenshot name of file is date
   * @author Giora Tovim
   */
  public String getScreenshot() throws IOException {
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    //The below method will save the screen shot in d drive with folder "screenshot" + filenameDate + ".png "
    String screenshotPath = reportDirectory + dateFormat.format(date) + ".png";
    try {
      File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(scrFile, new File(screenshotPath));
    } catch (NoSuchSessionException e) {
      e.printStackTrace();
      return "";
    }
    return screenshotPath;
  }

  /**
   * This method get screenshot name of file is date
   * @author Giora Tovim
   */
  public static String getScreenshot(WebDriver driver) throws Exception {
    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    //The below method will save the screen shot in d drive with folder "screenshot" + filenameDate + ".png"
    String screeshootPath = reportDirectory + dateFormat.format(date) + ".png";
    try {
      File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      FileUtils.copyFile(scrFile, new File(screeshootPath));
    } catch (NoSuchSessionException e) {
      e.printStackTrace();
      return "";
    }
    return screeshootPath;
  }

  /**
   * This method get screenshot for Allure report
   * @author Giora Tovim
   */
  public byte[] takeScreenshot(){
    return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
  }

  public LogEntries analyzeLog() {
    return driver.manage().logs().get(LogType.BROWSER);
  }

  private static String pathToDrivers(String fileName) {
    return java.nio.file.Paths.get(
        new File(System.getProperty("user.dir")).getPath(), "src", "main", "resources", "drivers",
        fileName).toString();
  }

    public static void restartDriver(WebDriver driver) {
        driver.manage().deleteAllCookies();
        driver.quit();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
}
