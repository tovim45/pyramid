package com.pyramid.modules;

import static com.pyramid.infra.logger.AutomationLogger.error;
import static com.pyramid.infra.logger.AutomationLogger.info;
import static java.lang.Thread.sleep;

import com.pyramid.infra.SeleniumUtils;
import com.pyramid.tests.BaseTest;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;


public abstract class BasePageOrig {

  public WebDriver driver = BaseTest.webDrivers.get(Thread.currentThread().getId());
  private String url;
  private String title;

  public BasePageOrig(String url, String title) {
    this.url = url;
    this.title = title;
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void navigate() {
    driver.get(this.url);
  }

  protected void validateTitle() {
    try {
      WebDriverWait wait = new WebDriverWait(driver, 60);
      wait.until(ExpectedConditions.titleIs(title));
    } catch (TimeoutException e) {
      error(String.format("Title should be %s, Browser on %S", title, driver.getTitle()));
      Assert.fail(String.format("Title should be %s, Browser on %S", title, driver.getTitle()), e);
    }
  }

  public void click(By loc) {
    WebElement element = getWebElement(loc, 50, ExpectedConditions.elementToBeClickable(loc));
    element.click();
    info("User clicks on: " + loc.toString());
  }

  /*
      In case StaleElementReferenceException exception occurs on click, please use this method !
   */
  public void clickStaleException(By loc) {
    int remainingAttempts = 50;

    while (remainingAttempts > 0) {
      remainingAttempts--;
      try {
        WebElement element = getWebElement(loc, 50, ExpectedConditions.elementToBeClickable(loc));
        info("User clicks on: " + loc.toString());
        element.click();
        break;
      } catch (StaleElementReferenceException e) {
        if (remainingAttempts == 0) {
          throw e;
        }
      }
    }
  }

  public void sendKeys(By loc, String text) {
    WebElement element = getWebElement(loc, 30, ExpectedConditions.elementToBeClickable(loc));
    element.clear();
    element.sendKeys(text);
    info("Send keys: " + text + " for: " + loc.toString());
  }

  public void sendKeys(By loc, Keys key) {
    WebElement element = getWebElement(loc, 30, ExpectedConditions.elementToBeClickable(loc));
    element.clear();
    element.sendKeys(key);
    info("Send keys: " + key + " for: " + loc.toString());
  }

  public void clearInputField(By loc) {
    WebElement element = getWebElement(loc, 30, ExpectedConditions.elementToBeClickable(loc));
    element.sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE));
    info("Input cleared for: " + loc.toString());
  }

  public void sendKeysNoClear(By loc, String text) {
    WebElement element = getWebElement(loc, 30, ExpectedConditions.elementToBeClickable(loc));
    element.sendKeys(text);
    info("Send keys: " + text + " for: " + loc.toString());
  }

  public boolean isDisplayed(By loc) {
    WebElement element = getWebElement(loc, 20, ExpectedConditions.presenceOfElementLocated(loc));
    boolean results = element.isDisplayed();
    info("Check is element: " + loc + " displayed: " + results);
    return results;
  }

  public void select(By loc, String value) {
    WebElement element = getWebElement(loc, 30, ExpectedConditions.presenceOfElementLocated(loc));
    ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block'", element);
    new Select(element).selectByValue(value);
  }

  protected String getText(By loc) {
    WebElement element = getWebElement(loc, 40, ExpectedConditions.presenceOfElementLocated(loc));
    String text = element.getText();
    info("User get text: " + text + " from this locator: " + loc.toString());
    return text;
  }

  protected String getValue(By loc) {
    WebElement element = getWebElement(loc, 40, ExpectedConditions.presenceOfElementLocated(loc));
    String text = element.getAttribute("Value");
    info("User get text: " + text + " from this locator: " + loc.toString());
    return text;
  }

  protected String getCSSValue(By loc, String value) {
    WebElement element = getWebElement(loc, 40, ExpectedConditions.presenceOfElementLocated(loc));
    String cssValue = element.getCssValue(value);
    info("User get : " + value + " " + cssValue + " from this locator: " + loc.toString());
    return cssValue;
  }

  protected Boolean isTextExistInWebPage(String textToFind, String textToAdd) throws Exception {
    Thread.sleep(5000);
    SeleniumUtils seleniumUtils = new SeleniumUtils();
    if (driver.getPageSource().contains(textToFind)) {
      info("Found " + textToAdd + textToFind + " successfully ");
      return true;
    } else {
      error("Did not found " + textToFind + " test failed");
      seleniumUtils.getScreenshot();
      return false;
    }

  }

  protected String getTextFromTable(By loc) throws InterruptedException {
    Thread.sleep(6000);
    WebElement element = getWebElement(loc, 30, ExpectedConditions.presenceOfElementLocated(loc));
    String text = "";
    List<WebElement> tr = element.findElements(By.tagName("tr"));
    for (WebElement w : tr) {
      text += w.getText();
    }
    return text;
  }

  protected int getRowNumberFromTable(By loc, String name) throws InterruptedException {
    Thread.sleep(2000);
    int i = 0;
    WebElement element = getWebElement(loc, 30, ExpectedConditions.presenceOfElementLocated(loc));
    List<WebElement> tr = element.findElements(By.tagName("tr"));
    for (WebElement w : tr) {
      if (w.getText().contains(name)) {
        return i;
      }
      i++;
    }
    return 0;
  }

  protected void switchToIframe(By loc) {
    WebElement element = getWebElement(loc, 10, ExpectedConditions.presenceOfElementLocated(loc));
    driver.switchTo().frame(element);
  }

  protected void exitIframe() {
    driver.switchTo().defaultContent();
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

  protected void waitForLoader(By loc) {
    try {
      WebDriverWait element = new WebDriverWait(driver, 18);
      element.until(ExpectedConditions.visibilityOfElementLocated(loc));
//            WebDriverWait wait = new WebDriverWait(driver, 18);
//            wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
    } catch (TimeoutException e) {

    }
  }

  //    public static void restartDriver() {
//        driver.manage().deleteAllCookies();         // Clear Cookies on the browser
//        driver.quit();
//        driver = null;
//        driver = new ChromeDriver();
//        driver.manage().window().maximize();
//
//    }
  protected void fileUpload(String path) throws AWTException {

    Robot robot = new Robot();
    robot.setAutoDelay(2000);
    StringSelection stringSelection = new StringSelection(path);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

    robot.setAutoDelay(2000);

    robot.keyPress(KeyEvent.VK_CONTROL);
    robot.keyPress(KeyEvent.VK_V);

    robot.keyRelease(KeyEvent.VK_CONTROL);
    robot.keyRelease(KeyEvent.VK_V);

    robot.setAutoDelay(2000);
    robot.keyPress(KeyEvent.VK_ENTER);
    robot.keyRelease(KeyEvent.VK_ENTER);
  }

  protected void hoverOnElement(By loc) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Actions builder = new Actions(driver);
    WebElement element = driver.findElement(loc);
    builder.moveToElement(element).perform();

  }

  protected String hoverOnElementGetTextValue(By loc) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Actions builder = new Actions(driver);
    WebElement element = driver.findElement(loc);
    builder.moveToElement(element).perform();
    return element.getAttribute("title");
  }

  protected void hoverAndClickOnElement(By loc) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Actions builder = new Actions(driver);
    WebElement element = driver.findElement(loc);
    builder.moveToElement(element).click().perform();
  }

  protected void dragAndDrop(By draggable, By droppable) {
    WebElement sourse = driver.findElement(draggable);
    WebElement target = driver.findElement(droppable);
    Actions builder = new Actions(driver);
    builder.dragAndDrop(sourse, target).build().perform();

  }

  protected void hoverAndClickOnElement(WebElement element) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Actions builder = new Actions(driver);
    builder.moveToElement(element).click().perform();
  }

  protected String clickElementAndSwitchToNewlyOpenedTab(By locator) {
    String initialTabWindowHandle = driver.getWindowHandle();
    List<String> initialOpenedTabsWindowHandles = new ArrayList<>(driver.getWindowHandles());
    click(locator);
    List<String> currentOpenedTabsWindowsHandles = new ArrayList<>(driver.getWindowHandles());
    currentOpenedTabsWindowsHandles.removeAll(initialOpenedTabsWindowHandles);
    String newlyOpenedTabWindowHandle = currentOpenedTabsWindowsHandles.get(0);
    driver.switchTo().window(newlyOpenedTabWindowHandle);
    return initialTabWindowHandle;
  }

  protected void clickElementUsingJS(By locator) {
    WebElement element = driver.findElement(locator);
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    executor.executeScript("arguments[0].click()", element);
  }

  protected WebElement getWebElement(By loc, int timeOut,
      ExpectedCondition<WebElement> expectedCon) {
    waitForElement(loc, timeOut, expectedCon);
    int count = 0;
    int maxTries = 3;
    while (true) {
      try {
        WebElement element = driver.findElement(loc);
        return element;
      } catch (StaleElementReferenceException e) {
        if (++count == maxTries) {
          throw e;
        }
      }
    }
  }

  protected List<WebElement> getWebElements(By loc, int timeOut,
      ExpectedCondition<WebElement> expectedCon) {
    waitForElement(loc, timeOut, expectedCon);
    return driver.findElements(loc);
  }

  protected List<WebElement> getWebElements(By locator) {
    return driver.findElements(locator);
  }

  protected WebElement getNestedWebElement(WebElement elem, By loc, int timeOut,
      ExpectedCondition<WebElement> expectedCon) {
    waitForElement(loc, timeOut, expectedCon);
    return elem.findElement(loc);
  }

  //    TODO complete method implementation
  protected void uploadFile(String filePath, WebElement dropZoneClass, String fileType)
      throws IOException {

    String id = dropZoneClass.getAttribute("id");
    String[] path = filePath.split("\\\\");
    String fileName = path[path.length - 1];
    String base64IFile = convertFileToBase64String(filePath);
    ((JavascriptExecutor) driver).executeScript("var myZone = Dropzone.forElement('#" + id + "');" +
        "base64Image = '" + base64IFile + "';" +
        "function base64toBlob(b64Data, contentType, sliceSize) {  \n" +
        "    contentType = contentType || '';\n" +
        "    sliceSize = sliceSize || 512;\n" +
        "\n" +
        "    var byteCharacters = atob(b64Data);\n" +
        "    var byteArrays = [];\n" +
        "\n" +
        "    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {\n" +
        "        var slice = byteCharacters.slice(offset, offset + sliceSize);\n" +
        "\n" +
        "        var byteNumbers = new Array(slice.length);\n" +
        "        for (var i = 0; i < slice.length; i++) {\n" +
        "            byteNumbers[i] = slice.charCodeAt(i);\n" +
        "        }\n" +
        "\n" +
        "        var byteArray = new Uint8Array(byteNumbers);\n" +
        "\n" +
        "        byteArrays.push(byteArray);\n" +
        "    }\n" +
        "\n" +
        "    var blob = new Blob(byteArrays, {type: contentType});\n" +
        "    return blob;\n" +
        "}" +
        "var blob = base64toBlob(base64Image, '" + fileType + "');" +
        "blob.name = '" + fileName + "';" +
        "myZone.addFile(blob);  "
    );
  }

  public static String convertFileToBase64String(String fileName) throws IOException {

    File file = new File(fileName);
    int length = (int) file.length();
    BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
    byte[] bytes = new byte[length];
    reader.read(bytes, 0, length);
    reader.close();
    String encodedFile = Base64.getEncoder().encodeToString(bytes);

    return encodedFile;
  }

  protected WebElement getElementsByClassJs(String className, int index) {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    return (WebElement) js.executeScript(
        "return document.getElementsByClassName('" + className + "')[" + index + "]");
  }

  protected void waitForElement(By loc, int timeOut, ExpectedCondition<?> expectedCon) {
    try {
      WebDriverWait wait = new WebDriverWait(driver, timeOut);
      wait.until(expectedCon);
    } catch (TimeoutException e) {
      error("Element Not Found For Locator: " + loc.toString());
      Assert.fail("Element Not Found For Locator: " + loc.toString(), e);
    }
  }

  /**
   * Method selects option if exists from the dropdown
   *
   * @param dropdown - arrow icon with class attribute 'k-icon k-i-arrow-s comboArrowClick'
   * @param option   to select
   * @throws Exception
   */
  protected void dropdownSelect(WebElement dropdown, String option) throws Exception {

    dropdown.click();
    String optionsIdentifier = dropdown.getAttribute("aria-controls");

    List<WebElement> options = driver
        .findElements(By.xpath("//*[contains(@id, '" + optionsIdentifier + "')]//li"));

    for (Iterator<WebElement> iterator = options.iterator(); iterator.hasNext(); ) {
      WebElement webElement = (WebElement) iterator.next();
      if (webElement.getAttribute("innerText").equals(option)) {
        webElement.click();
        return;
      }
    }
  }


  /**
   * Method checks if option exists in dropdown
   *
   * @param dropdown - arrow icon with class attribute 'k-icon k-i-arrow-s comboArrowClick'
   * @param option
   * @return true if exists, false otherwise
   * @throws Exception
   */
  protected boolean isDropdownOptionExists(WebElement dropdown, String option) throws Exception {

    dropdown.click();
    String optionsIdentifier = dropdown.getAttribute("aria-controls");

    List<WebElement> options = driver
        .findElements(By.xpath("//*[contains(@id, '" + optionsIdentifier + "')]//li"));

    for (Iterator<WebElement> iterator = options.iterator(); iterator.hasNext(); ) {
      WebElement webElement = (WebElement) iterator.next();
      if (webElement.getAttribute("innerText").equals(option)) {
        return true;
      }
    }
    return false;
  }


  /**
   * Method selects option if exists from the dropdown
   *
   * @param dropdown - arrow icon with class attribute 'k-icon k-i-arrow-s comboArrowClick'
   * @param option   to select
   * @throws Exception
   */
  protected void dropListSelect(WebElement dropdown, String option) throws Exception {
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

  protected void checkBoxSelectValues(WebElement checkbox, List<String> values) throws Exception {
    checkbox.click();

    List<WebElement> options = driver.findElements(By.xpath("//li[@data-value]"));

    for (String value : values) {
      for (Iterator<WebElement> iterator = options.iterator(); iterator.hasNext(); ) {
        WebElement webElement = iterator.next();
        if (webElement.getAttribute("innerText").equalsIgnoreCase(value)) {
          webElement.click();
        }
      }
    }
    options.get(0).sendKeys(Keys.ESCAPE);
  }

  /**
   * Checks if field is labeled as required. The method uses the fact that kandoo ui assigns
   * speciffic class to required field's lables
   *
   * @param fieldLable without asterisk
   * @return true if field is required, false otherwise
   */
  public boolean isRequired(String fieldLable) {
//		FindBy(how = How.XPATH, using = "//label[contains(@class, 'ng-binding required-asterisk')]")
//		static WebElement requiredLabels;

    List<WebElement> requiredFields = driver
        .findElements(By.xpath("//label[contains(@class, 'ng-binding required-asterisk')]"));
    for (WebElement required : requiredFields) {
      if (required.getAttribute("innerText").equals(fieldLable)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method waits for web element identified by locator to appear and clicks on it
   *
   * @param locator
   * @param maxTimeout
   * @return WebElement
   * @throws Exception
   * @author FredS3
   */
  protected WebElement actionClick(By locator, int maxTimeout) throws Exception {

    WebElement webEl = driver.findElement(locator);

    //scrollToElement(locator);
    Actions builder = new Actions(driver);
    while (!webEl.getAttribute("class").contains("k-edit-cell")) {
      builder.moveToElement(webEl).click().perform();
    }

    return webEl;
  }

  /**
   * Method waits for web element to appear and clicks on it
   *
   * @param element
   * @param maxTimeout
   * @return element
   * @throws Exception
   * @author FredS3
   */
  protected WebElement actionClick(WebElement element, int maxTimeout) throws Exception {
    //scrollToElement(element);
    Actions builder = new Actions(driver);
    //try to click on grid cell until it's selected
    while (!element.getAttribute("class").contains("k-edit-cell")) {
      builder.moveToElement(element).click().perform();
    }

    return element;
  }

  protected void actionsClick(By locator) {
    WebElement webElement = driver.findElement(locator);
    Actions actions = new Actions(driver);
    actions.moveToElement(webElement).click().perform();
  }

  protected void clickStaleElementReferenceException(By locator) {
    long startTime = System.currentTimeMillis();
    long elapsedTime = 0L;
    while (elapsedTime < 10 * 1000L) {
      try {
        driver.findElement(locator).click();
        return;
      } catch (StaleElementReferenceException e) {
        logInfo(true, "StaleElementReferenceException caught. Retrying click() method.");
      }
      elapsedTime = System.currentTimeMillis() - startTime;
    }
  }

  protected void waitAFewSeconds(int timeoutInSeconds) {
    try {
      sleep(timeoutInSeconds * 1000L);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      e.printStackTrace();
    }
  }

  /**
   * Method check within maxTimeout if element identified by locator enabled
   *
   * @param locator
   * @param maxTimeout
   * @return true if element enabled, false otherwise
   * @throws Exception
   * @author FredS3
   */
  protected boolean isEnabled(By locator, int maxTimeout) throws Exception {

    WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
    WebElement webEl = driver.findElement(locator);
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
      if (webEl.isEnabled() && webEl.getAttribute("class").equals("k-state-disabled")) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Method check within maxTimeout if element identified by locator exists
   *
   * @param locator
   * @param maxTimeout
   * @return true if element exists, false otherwise
   * @throws Exception
   * @author FredS3
   */
  protected boolean isExists(By locator, int maxTimeout) throws Exception {

    WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
    try {
      wait.until(ExpectedConditions.presenceOfElementLocated(locator));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Method wait's till element is clickable
   *
   * @param element
   * @param maxTimeout
   */
  protected void waitTillClickable(WebElement element, int maxTimeout) {
    WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
    wait.until(ExpectedConditions.elementToBeClickable(element));
  }

  /**
   * The method scrolls to given element by locator
   *
   * @param locator
   * @author FredS3
   */
  protected void scrollToElement(By locator) {

    WebElement webEl = driver.findElement(locator);

    JavascriptExecutor js = (JavascriptExecutor) driver;
    try {
      js.executeScript("arguments[0].scrollIntoView(true);", webEl);
    } catch (Exception e) {
      //ignore error
    }
  }

  /**
   * The method scrolls to given web element
   *
   * @param element
   * @author FredS3
   */
  protected void scrollToElement(WebElement element) {

    int yCoordinate;
    WebElement webEl = element;

    yCoordinate = webEl.getLocation().getY();
    JavascriptExecutor js = (JavascriptExecutor) driver;
    js = (JavascriptExecutor) driver;
    try {
      js.executeScript("window.scrollTo(0," + yCoordinate + ")");
    } catch (Exception e) {
      //ignore error
    }
  }

  /**
   * Method sets text in web element identified by any locator
   *
   * @param locator    of type By
   * @param text       to set
   * @param maxTimeout time to wait for element to apear
   */
  protected void setTextBoxText(By locator, String text, int maxTimeout) {

    WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
    wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

    //scrollToElement(locator);
    WebElement elem = driver.findElement(locator);
    Actions builder = new Actions(driver);
    try {
      elem.clear();
    } catch (Exception e) {
      //ignore
    }
    Actions typeText = builder.moveToElement(elem).click().sendKeys(elem, text);
    typeText.perform();
    driver.switchTo().defaultContent();
  }

  /**
   * Method sets text in web element
   *
   * @param element
   * @param text
   * @param maxTimeout
   */
  protected void setTextBoxText(WebElement element, String text, int maxTimeout) {

    WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
    wait.until(ExpectedConditions.visibilityOfElementLocated(toByVal(element)));

    //scrollToElement(locator);
    WebElement elem = element;
    Actions builder = new Actions(driver);
    try {
      elem.clear();
    } catch (Exception e) {
      //ignore
    }
    Actions typeText = builder.moveToElement(elem).click().sendKeys(elem, text);
    typeText.perform();
    driver.switchTo().defaultContent();
  }


  /**
   * Receives web element and returns its By type
   *
   * @param we of type WebElement
   * @return ByType of WebElement
   */
  protected By toByVal(WebElement we) {
    // By format = "[foundFrom] -> locator: term"
    // see RemoteWebElement toString() implementation
    String str = we.toString().split(" -> ")[1];
    String[] data = str.substring(0, str.length() - 1).split(": "); //remove last char
    //String[] data = we.toString().split(" -> ")[1].replace("]", "").split(": ");
    String locator = data[0];
    String term = data[1];
    // remove possible duplication of ] in term
    while (term.endsWith("]]")) {
      term = term.substring(0, term.length() - 1);
    }

    return switch (locator) {
      case "xpath" -> By.xpath(term);
      case "css selector" -> By.cssSelector(term);
      case "id" -> By.id(term);
      case "tag name" -> By.tagName(term);
      case "name" -> By.name(term);
      case "link text" -> By.linkText(term);
      case "class name" -> By.className(term);
      default -> (By) we;
    };
  }

  /**
   * @method : Method describe wait for 20 sec to visible the element
   */
  protected void waitUntilPageLoad(By loc) {
    WebDriverWait element = new WebDriverWait(driver, 10);
    WebDriverWait wait = new WebDriverWait(driver, 10);
    try {
      element.until(ExpectedConditions.visibilityOfElementLocated(loc));
      wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
    } catch (Exception e) {
      //ignore
    }
  }

  /**
   * @method : This Method delete the values from the text box by selecting all the elements
   */
  protected void clearTextBoxValue(By loc) {
    WebElement element = driver.findElement(loc);
    try {
      element.sendKeys(Keys.CONTROL + "a");
      element.sendKeys(Keys.DELETE);
    } catch (Exception e) {
      //ignore
    }
  }

  /**
   * @method : This method describe scroll to the bottom of the page
   */
  protected void scrollToHeight() {
    JavascriptExecutor js = (JavascriptExecutor) driver;
    try {
      js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    } catch (Exception e) {
      //ignore error
    }
  }

  /**
   * @method : This method check the details and return false if it is not present
   */
  protected static boolean assertRowContains(String expectedResult, String actual) {
    if (!actual.contains(expectedResult)) {
      info(" ----- Text expected: " + expectedResult + " Was: " + actual + " -----");
      return false;
    }
    return true;
  }

  /**
   * @Method : This method verify the exact text from the list of details
   */
  protected static boolean isContain(String source, String subItem) {
    String pattern = "\\b" + subItem + "\\b";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(source);
    return m.find();
  }

  /**
   * This method validate the file is exists in the directory or not
   *
   * @author Prashant Lokahnde
   */
  protected boolean isFileExists(File file, int maxTimeout) {
    WebDriverWait wait = new WebDriverWait(driver, maxTimeout);
    try {
      wait.until((ExpectedCondition<Boolean>) webDriver -> file.exists());
      return true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public int getTblPagination(String name, By pages, By tableName) throws Exception {
    int actualRow;
    WebElement element = getWebElement(pages, 30,
        ExpectedConditions.presenceOfElementLocated(pages));
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
        WebElement element2 = getWebElement(pages, 30,
            ExpectedConditions.presenceOfElementLocated(pages));
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

  public boolean isPresent(By loc) {
    info("Check if element: " + loc + " present: ");
    if (driver.findElement(loc) != null) {
      return true;
    }
    return false;
  }

  public void waitAndAssertElementsListIsDisplayed(List<By> elementLocatorsList) {
    elementLocatorsList.forEach(x -> {
      waitForLoader(x);
      Assert.assertTrue(isDisplayed(x), "Did not found element: " + x);
    });
  }

  protected void logInfo(boolean printToConsole, String message) {
    info(message);
    if (printToConsole) {
      info(message);
    }
  }

  protected void logFail(boolean printToConsole, String message) {
    error(message);
    if (printToConsole) {
      info(message);
    }
  }

  protected void logPass(boolean printToConsole, String message) {
    info(message);
    if (printToConsole) {
      info(message);
    }
  }
}
