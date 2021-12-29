//package com.pyramid.tests.android.examples;
//
//import com.pyramid.pages.andorid.LoginPageP;
//import com.pyramid.tests.BaseTest;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.remote.AndroidMobileCapabilityType;
//import io.appium.java_client.remote.MobileCapabilityType;
//import java.net.MalformedURLException;
//import java.net.URL;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//public class appiumBankP extends BaseTest {
//    private String reportDirectory = "C:\\Test\\AppiumReports";
//    private String reportFormat = "xml";
//    private String testName = "MarketPlace.html";
//    protected AndroidDriver driver = null;
//    private String udid = "emulator-5554";
//    private String appPackage = "com.experitest.ExperiBank";
//    private String appActivity = ".LoginActivity";
//    private boolean noReset = true;
//    private String url = "http://10.160.11.64:4723/wd/hub";
//
//    @BeforeClass
//    public void setUp() throws MalformedURLException, Exception {
//        DesiredCapabilities dc = new DesiredCapabilities();
//        dc.setCapability("reportDirectory", reportDirectory);
//        dc.setCapability("reportFormat", reportFormat);
//        dc.setCapability("testName", testName);
//        dc.setCapability("noreset", noReset);
//        dc.setCapability(MobileCapabilityType.DEVICE_NAME, udid);
//        dc.setCapability(MobileCapabilityType.APP, "C:/apk/EriBank.apk");
//        dc.setCapability(AndroidMobileCapabilityType.PLATFORM, "android");
//        dc.setCapability(MobileCapabilityType.UDID, udid);
//        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, appPackage);
//        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, appActivity);
//        driver = new AndroidDriver<>(new URL(url), dc);
//    }
//
//    @Test(priority = 0, testName = "demo")
//    public void loginTestP() throws Exception {
//
//        LoginPageP page = new LoginPageP(driver);
//        page.typeUsername("prashant");
//        page.typePassword("123456");
//        page.clickLogin();
//
//        Thread.sleep(3000);
//        driver.quit();
//    }
//}
