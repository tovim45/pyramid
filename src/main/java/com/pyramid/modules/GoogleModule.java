package com.pyramid.modules;

import static com.pyramid.infra.logger.AutomationLogger.info;

import com.pyramid.tests.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;


public class GoogleModule extends BaseModule {

  private static final String URL = BaseTest.property.getProperty("googleUrl");
  private static final String TITLE = "google";

  private final By findText = By.name("q");

  public GoogleModule() {
    super(URL, TITLE);
  }

  public void findTextGoogle(String textToFind) {
    info("--------------------------- google find POC ------------------------------");
    info("Text to find: " + textToFind);
    navigate();

    Assert.assertEquals("google", TITLE);
    sendKeys(findText, textToFind);

  }


}
