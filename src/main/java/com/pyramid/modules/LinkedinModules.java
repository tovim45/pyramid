package com.pyramid.modules;

import com.pyramid.tests.BaseTest;
import org.openqa.selenium.By;

public class LinkedinModules extends BaseModule {

  private static final String URL = BaseTest.property.getProperty("linkedinUrl");
  private static final String USER = BaseTest.property.getProperty("user");
  private static final String PASS = BaseTest.property.getProperty("pass");
  private static final String TITLE = "linkedin";

  private final By username = By.id("session_key");
  private final By password = By.id("session_password");
  private final By login = By.xpath("//button[@type='submit']");
  private final By me = By.xpath("//span[text()='Me']");
  private final By profile = By.xpath("//*[text()='View Profile']");
  private final By connections = By.xpath("//*[text()=' connections']");


  public LinkedinModules() {
    super(URL, TITLE);
    navigate();
  }

  public void linkedinLogin() {
    sendKeys(username, USER);
    sendKeys(password, PASS);
    click(login);
  }

  public void clickMe() {
    click(me);
  }

  public void clickProfile() {
    click(profile);
  }

  public void clickConnections() {
    click(connections);
  }

  public boolean linkedinVerifyConnectionName(String connectionName) {
    waitAFewSeconds(2);
    return checkTextPresenceInWebpage(connectionName);
  }
}
