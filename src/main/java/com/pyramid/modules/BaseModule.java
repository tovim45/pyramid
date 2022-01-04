package com.pyramid.modules;

import static com.pyramid.infra.logger.AutomationLogger.debug;
import static com.pyramid.infra.logger.AutomationLogger.error;
import static com.pyramid.infra.logger.AutomationLogger.info;
import static com.pyramid.infra.logger.AutomationLogger.trace;

import com.pyramid.tests.BaseTest;
import com.pyramid.utils.ThreadUtils;
import io.qameta.allure.Step;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public abstract class BaseModule {

  public WebDriver driver = BaseTest.webDrivers.get(Thread.currentThread().getId());
  private String url;
  private String title;
  private static final String CLICK = "Click: ";
  private static final String RETURNED = ") returned: ";
  private final int WAIT_TIMEOUT_IN_SECONDS = 2;

  public BaseModule(String url, String title) {
    this.url = url;
    this.title = title;
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void navigate() {
    driver.get(this.url);
  }

  @Step("Refresh webpage")
  public void refreshWebpage() {
    debug("Refresh webpage: " + driver.getCurrentUrl());
    driver.navigate().refresh();
  }

  protected WebElement getWebElementNoWait(By locator) {
    trace("getWebElementNoWait(By locator): locator=" + locator.toString());
    return driver.findElement(locator);
  }

  protected WebElement getWebElement(By locator) {
    trace("getWebElement(By locator): locator=" + locator.toString());
    waitUntilElementIsVisible(locator);
    return driver.findElement(locator);
  }

  protected List<WebElement> getWebElements(By locator) {
    trace("getWebElements(By locator): locator=" + locator.toString());
    return driver.findElements(locator);
  }

  protected List<WebElement> getWebElementsWithWait(By locator) {
    trace("getWebElementsWithWait(By locator): locator=" + locator.toString());
    waitUntilElementIsVisible(locator);
    return getWebElements(locator);
  }

  protected By getByFromWebElement(WebElement element) {
    trace("getByFromWebElement(WebElement element): element=" + element.toString());
    String[] locatorData = (element.toString().split("->")[1].replaceAll(".$", "")).split(":");
    String locatorType = locatorData[0].trim();
    String locatorValue = locatorData[1].trim();
    return switch (locatorType) {
      case "id" -> By.id(locatorValue);
      case "className" -> By.className(locatorValue);
      case "tagName" -> By.tagName(locatorValue);
      case "xpath" -> By.xpath(locatorValue);
      case "cssSelector" -> By.cssSelector(locatorValue);
      case "linkText" -> By.linkText(locatorValue);
      case "name" -> By.name(locatorValue);
      case "partialLinkText" -> By.partialLinkText(locatorValue);
      default -> throw new IllegalStateException(
          "::: ERROR ::: getByFromWebElement(WebElement element) :::" +
              " No implementation found for locatorType: " + locatorType);
    };
  }

  protected String getStringValueFromBy(By locator) {
    trace("getStringValueFromBy(By locator): locator=" + locator.toString());
    return locator.toString().split(": ", 2)[1];
  }

  protected String getStringValueFromWebElement(WebElement element) {
    trace("getStringValueFromElement(WebElement element): element=" + element.toString());
    return StringUtils.chop(element.toString().split(": ", 3)[2]);
  }

  protected void click(By locator) {
    debug(CLICK + locator);
    long startTime = System.currentTimeMillis();
    var elapsedTime = 0L;
    while (elapsedTime < WAIT_TIMEOUT_IN_SECONDS * 1000L) {
      try {
        privateClick(locator);
        return;
      } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
        error(e.toString());
        error("Exception caught. Retrying click() method.");
      }
      elapsedTime = System.currentTimeMillis() - startTime;
    }
    privateClick(locator);
  }

  protected void click(WebElement element) {
    trace("click(WebElement element): element=" + element.toString());
    click(getByFromWebElement(element));
  }

  /**
   * Click method used for WebElements retrieved with getWebElements/getWebElementsWithWait
   * methods.
   */
  protected void clickElement(WebElement element) {
    debug(CLICK + element.toString());
    waitUntilElementIsClickable(element);
    element.click();
  }

  private void privateClick(By locator) {
    trace("privateClick(By locator): locator=" + locator.toString());
    waitUntilElementIsClickable(locator);
    getWebElement(locator).click();
  }

  /**
   * Click method used for checkbox, toggle switch.
   */
  protected void clickJS(By locator) {
    clickJS(driver.findElement(locator));
  }

  protected void clickJS(WebElement element) {
    debug(CLICK + getStringValueFromBy(getByFromWebElement(element)));
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].click()", element);
  }

  protected void clearInputField(By locator) {
    debug("Clear input field: " + getStringValueFromBy(locator));
    getWebElement(locator).sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
  }

  protected void sendKeys(By locator, String text) {
    trace("sendKeys(By locator, String text): locator=" + locator.toString() + " keys=" + text);
    sendKeys(getWebElement(locator), text);
  }

  protected void sendKeys(By locator, Keys keys) {
    trace("sendKeys(By locator, Keys keys): locator=" + locator.toString() + " keys=" + keys);
    sendKeys(getWebElement(locator), keys);
  }

  protected void sendKeys(WebElement element, String text) {
    debug("Send keys: " + text + " to: " + getStringValueFromWebElement(element));
    element.sendKeys(text);
  }

  protected void sendKeys(WebElement element, Keys keys) {
    debug("Send keys: " + keys + " to: " + getStringValueFromWebElement(element));
    element.sendKeys(keys);
  }

  /**
   * Scroll vertically to element.
   */
  protected void scrollVertically(By locator) {
    debug("Scroll vertically to element: " + getStringValueFromBy(locator));
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].scrollIntoView(true);", getWebElement(locator));
  }

  /**
   * Scroll vertically a given number of pixels.
   */
  protected void scrollVertically(String jsLocator, int nrOfPixel) {
    debug("Scroll vertically " + nrOfPixel + " pixels on: " + jsLocator);
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("document.querySelector(arguments[0]).scrollBy(0,arguments[1]);",
        jsLocator, nrOfPixel);
  }

  /**
   * Scroll horizontally to element.
   */
  protected void scrollHorizontally(By locator) {
    debug("Scroll horizontally to element: " + getStringValueFromBy(locator));
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].scrollIntoView({block: \"end\" ,inline: \"center\"});",
        getWebElement(locator));
  }

  protected void hoverOnElement(By locator) {
    trace("hoverOnElement(By locator): locator=" + locator.toString());
    hoverOnElement(getWebElement(locator));
  }

  protected void hoverOnElement(WebElement element) {
    debug("Hover on element: " + getStringValueFromWebElement(element));
    var actions = new Actions(driver);
    actions.moveToElement(element).perform();
  }

  /**
   * Click keyDown and enter
   */
  protected void keyDownAndEnter() {
    var actions = new Actions(driver);
    actions.sendKeys(Keys.DOWN).perform();
    actions.sendKeys(Keys.RETURN).perform();
  }

  protected Boolean checkTextPresenceInWebpage(String text) {
    boolean result = driver.getPageSource().contains(text);
    debug("checkTextPresenceInWebpage(" + text + RETURNED + result);
    return result;
  }

  /**
   * Wait for the element to load and check if it is visible on the page. To be used when the
   * element is expected to be present in the DOM.
   *
   * @param locator of the element
   * @return true if the element is visible on the page, otherwise false
   */
  protected boolean isDisplayed(By locator) {
    trace("isDisplayed(By locator): locator=" + locator.toString());
    return isDisplayed(getWebElement(locator));
  }

  protected boolean isDisplayed(WebElement element) {
    boolean result = element.isDisplayed();
    debug("isDisplayed(" + getStringValueFromWebElement(element) + RETURNED + result);
    return result;
  }

  /**
   * Check if the element is present in the DOM.
   *
   * @param locator of the element
   * @return true if the element is present in the DOM, otherwise false
   */
  protected boolean isPresent(By locator) {
    boolean result = !isNotPresent(locator);
    debug("isPresent(" + getStringValueFromBy(locator) + RETURNED + result);
    return result;
  }

  /**
   * Check if the element is not present in the DOM.
   *
   * @param locator of the element
   * @return true if the element is not present in the DOM, otherwise false
   */
  protected boolean isNotPresent(By locator) {
    boolean result = getWebElements(locator).isEmpty();
    debug("isNotPresent(" + getStringValueFromBy(locator) + RETURNED + result);
    return result;
  }

  protected void makeElementVisible(String jsLocator) {
    debug("Make element visible: " + jsLocator);
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("document.querySelector(arguments[0]).style.visibility = 'visible'",
        jsLocator);
  }

  protected String getAttribute(By locator, String attribute) {
    trace("getAttribute(By locator, String attribute): locator=" + locator.toString()
        + " attribute=" + attribute);
    return getAttribute(getWebElement(locator), attribute);
  }

  protected String getAttribute(WebElement element, String attribute) {
    waitUntilElementIsVisible(element);
    String result = element.getAttribute(attribute);
    debug("getAttribute(" + getStringValueFromWebElement(element) + ", " + attribute + RETURNED
        + result);
    return result;
  }

  protected String getAttributeValue(By locator) {
    String result = getAttribute(locator, "value");
    debug("getAttributeValue(" + getStringValueFromBy(locator) + RETURNED + result);
    return result;
  }

  protected String getAttributeStyle(By locator) {
    String result = getAttribute(locator, "style");
    debug("getAttributeStyle(" + getStringValueFromBy(locator) + RETURNED + result);
    return result;
  }

  protected String getAttributeInnerText(By locator) {
    String result = getAttribute(locator, "innerText");
    debug("getAttributeInnerText(" + getStringValueFromBy(locator) + RETURNED + result);
    return result;
  }

  protected String getAttributeInnerText(WebElement element) {
    String result = getAttribute(element, "innerText");
    debug("getAttributeInnerText(" + getStringValueFromWebElement(element) + RETURNED + result);
    return result;
  }

  private void waitUntilExpectedConditions(Function<WebDriver, WebElement> expectedConditions) {
    var wait = new WebDriverWait(driver, WAIT_TIMEOUT_IN_SECONDS);
    wait.until(expectedConditions);
  }

  private void waitUntilExpectedConditions(ExpectedCondition<Boolean> expectedConditions) {
    var wait = new WebDriverWait(driver, WAIT_TIMEOUT_IN_SECONDS);
    wait.until(expectedConditions);
  }

  protected void waitUntilElementIsVisible(By locator) {
    trace("waitUntilElementIsVisible(By locator): locator=" + locator.toString());
    waitUntilExpectedConditions(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected void waitUntilElementIsInvisible(By locator) {
    trace("waitUntilElementIsInvisible(By locator): locator=" + locator.toString());
    waitUntilExpectedConditions(ExpectedConditions.invisibilityOfElementLocated(locator));
  }

  protected void waitUntilElementIsInvisible(WebElement element) {
    trace("waitUntilElementIsInvisible(WebElement element): element=" + element.toString());
    waitUntilExpectedConditions(ExpectedConditions.invisibilityOf(element));
  }

  protected void waitUntilElementIsVisible(By locator, int maxTimeoutInSeconds) {
    WebDriverWait element = new WebDriverWait(driver, maxTimeoutInSeconds);
    element.until(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  protected void waitUntilElementIsVisible(WebElement element) {
    trace("waitUntilElementIsVisible(WebElement element): element=" + element.toString());
    waitUntilExpectedConditions(ExpectedConditions.visibilityOf(element));
  }

  protected void waitUntilElementIsClickable(By locator) {
    trace("waitUntilElementIsClickable(By locator): locator=" + locator.toString());
    waitUntilExpectedConditions(ExpectedConditions.elementToBeClickable(locator));
  }

  protected void waitUntilElementIsClickable(WebElement element) {
    trace("waitUntilElementIsClickable(WebElement element): element=" + element.toString());
    waitUntilExpectedConditions(ExpectedConditions.elementToBeClickable(element));
  }

  public void waitAFewSeconds(int timeoutInSeconds) {
    trace("waitAFewSeconds(int timeoutInSeconds): timeoutInSeconds=" + timeoutInSeconds);
    ThreadUtils.sleepXSeconds(timeoutInSeconds);
  }

  protected String getCSSValue(By locator, String value) {
    String result = getWebElement(locator).getCssValue(value);
    debug("getCSSValue(" + getStringValueFromBy(locator) + ", " + value + RETURNED + result);
    return result;
  }

  protected String getText(By locator) {
    trace("getText(By locator): locator=" + locator.toString());
    long startTime = System.currentTimeMillis();
    var elapsedTime = 0L;
    while (elapsedTime < WAIT_TIMEOUT_IN_SECONDS * 1000L) {
      try {
        return privateGetText(locator);
      } catch (StaleElementReferenceException e) {
        error(e.toString());
        error("Exception caught. Retrying getText() method.");
      }
      elapsedTime = System.currentTimeMillis() - startTime;
    }
    return privateGetText(locator);
  }

  private String privateGetText(By locator) {
    trace("privateGetText(By locator): locator=" + locator.toString());
    return getText(getWebElement(locator));
  }

  protected String getText(WebElement element) {
    waitUntilElementIsVisible(element);
    String result = element.getText();
    debug("getText(" + getStringValueFromWebElement(element) + RETURNED + result);
    return result;
  }

  protected String getTextNoWait(WebElement element) {
    String result = element.getText();
    debug("getTextNoWait(" + getStringValueFromWebElement(element) + RETURNED + result);
    return result;
  }

  protected String getTextNoWait(By locator) {
    String result = driver.findElement(locator).getText();
    debug("getTextNoWait(" + getStringValueFromBy(locator) + RETURNED + result);
    return result;
  }

  protected By createLocatorFromValue(By locator, String... value) {
    By result = By.xpath(String.format(getStringValueFromBy(locator), value));
    debug("createLocatorFromValue(" + getStringValueFromBy(locator) + ", " + value + RETURNED
        + result);
    return result;
  }

  protected By createLocatorFromValue(String locator, String... value) {
    trace("createLocatorFromValue(String locator, String... value): locator=" + locator + " value="
        + value);
    return createLocatorFromValue(By.xpath(locator), value);
  }

  protected By createLocatorFromValue(By locator, int value) {
    By result = By.xpath(String.format(getStringValueFromBy(locator), value));
    debug("createLocatorFromValue(" + getStringValueFromBy(locator) + ", " + value + RETURNED
        + result);
    return result;
  }

  protected By createLocatorFromValue(String locator, int value) {
    trace("createLocatorFromValue(String locator, int value): locator=" + locator + " value="
        + value);
    return createLocatorFromValue(By.xpath(locator), value);
  }

  protected String getPageTitle() {
    String result = driver.getTitle();
    debug("getPageTitle(" + RETURNED + result);
    return result;
  }

  /**
   * Click an element and switch to the newly created tab. Returns the window handle of the initial
   * tab.
   *
   * @param locator of the element that will trigger a new tab creation
   * @return window handle of the initial tab
   */
  protected String clickAndSwitchToNewTab(By locator) {
    debug("Current tab: " + getPageTitle());
    String initialWindowHandle = driver.getWindowHandle();
    List<String> beforeTabList = new ArrayList<>(driver.getWindowHandles());
    clickJS(locator);
    List<String> afterTabList = new ArrayList<>(driver.getWindowHandles());
    afterTabList.removeAll(beforeTabList);
    driver.switchTo().window(afterTabList.get(0));
    debug("Switched to tab: " + getPageTitle());
    return initialWindowHandle;
  }

  protected void switchToPreviousTab() {
    ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
    driver.switchTo().window(tabs2.get(1));
    driver.close();
    driver.switchTo().window(tabs2.get(0));
  }

  protected void switchToNextTab(int tabNr) {
    ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
    driver.switchTo().window(tabs2.get(tabNr));
  }

  protected void switchToIframe(By locator) {
    debug("Switch to iframe: " + getStringValueFromBy(locator));
    driver.switchTo().frame(getWebElement(locator));
  }

  protected void exitIframe() {
    debug("Exit iframe.");
    driver.switchTo().defaultContent();
  }

  /***
   * Used specifically for selecting 'option' elements inside 'select' elements.
   */
  protected void selectOptionFromDropdown(By dropdownLocator, String option) {
    Select dropdown = new Select(getWebElement(dropdownLocator));
    dropdown.selectByVisibleText(option);
  }


  /**
   * Method Click and select option from the dropdown
   * @author Giora Tovim
   */
  protected void clickAndSelectOptionFromDropdown(WebElement dropdown, String option) {
    dropdown.click();
    List<WebElement> options = driver.findElements(By.xpath("//li[@data-value]"));
    for (Iterator<WebElement> iterator = options.iterator(); iterator.hasNext(); ) {
      WebElement webElement = (WebElement) iterator.next();
      if (webElement.getAttribute("innerText").equalsIgnoreCase(option)) {
        webElement.click();
        return;
      }
    }
  }

  /**
   * Select values in checkbox
   * @author Giora Tovim
   */
  protected void checkBoxSelectValues(WebElement checkbox, List<String> values) throws Exception {
    checkbox.click();
    List<WebElement> options = driver.findElements(By.xpath("//li[@data-value]"));
    values.forEach(value -> {
      for (WebElement webElement : options) {
        if (webElement.getAttribute("innerText").equalsIgnoreCase(value)) {
          webElement.click();
        }
      }
    });
    options.get(0).sendKeys(Keys.ESCAPE);
  }

  /**
   * Get all rows table data
   * @author Giora Tovim
   */
  public int getTblPagination(String name, By pages, By tableName) throws Exception {
    int actualRow;
    WebElement element = getWebElement(pages);
    List<WebElement> page = element.findElements(pages);
    for (int k = 0; k < page.size(); k++) {
      try {
        String text = page.get(k).getText();
        info("btn text :" + text);
        if (!(text.equals("")) || (text.equals("1"))) {
          page = element.findElements(pages);
          page.get(k).click();
        }
      } catch (StaleElementReferenceException ex) {
        WebElement element2 = getWebElement(pages);
        List<WebElement> page2 = element2.findElements(pages);
        String text = page2.get(k).getText();
        info("btn text :" + text);
        if (!(text.equals("")) || (text.equals("1"))) {
          page2.get(k).click();
        }
      }
      actualRow = getRowNumberFromTable(tableName, name);
      if (actualRow != 0) {
        return actualRow;
      }
    }
    return 0;
  }

  /**
   * Get text from table
   * @author Giora Tovim
   */
  protected String getTextFromTable(By loc) throws InterruptedException {
    Thread.sleep(6000);
    WebElement element = getWebElement(loc);
    String text = "";
    List<WebElement> tr = element.findElements(By.tagName("tr"));
    for (WebElement w : tr) {
      text += w.getText();
    }
    return text;
  }

  /**
   * Get row table data
   * @author Giora Tovim
   */
  protected int getRowNumberFromTable(By loc, String name) throws InterruptedException {
    Thread.sleep(2000);
    int i = 0;
    WebElement element = getWebElement(loc);
    List<WebElement> tr = element.findElements(By.tagName("tr"));
    for (WebElement w : tr) {
      if (w.getText().contains(name)) {
        return i;
      }
      i++;
    }
    return 0;
  }

  public void fileUpload(String path) {
    StringSelection strSelection = new StringSelection(path);
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    clipboard.setContents(strSelection, null);
    Robot robot = null;
    try {
      robot = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }
    robot.delay(300);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
    robot.keyPress(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_V);
    robot.keyRelease(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.delay(200);
    robot.keyRelease(KeyEvent.VK_ENTER);
    waitAFewSeconds(1);
  }
}
