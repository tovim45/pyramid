package com.pyramid.tests.android.examples;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AppiumBank2 {


    public class Untitled {
        private String reportDirectory = "reports";
        private String reportFormat = "xml";
        private String testName = "Untitled";
        protected AndroidDriver<AndroidElement> driver = null;

        DesiredCapabilities dc = new DesiredCapabilities();

        @BeforeMethod
        public void setUp() throws MalformedURLException {
            dc.setCapability("reportDirectory", reportDirectory);
            dc.setCapability("reportFormat", reportFormat);
            dc.setCapability("testName", testName);
            dc.setCapability(MobileCapabilityType.DEVICE_NAME, "CM5");
            dc.setCapability(MobileCapabilityType.UDID, "401-686-709");
            dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
            dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
            driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), dc);
            driver.setLogLevel(Level.INFO);
        }

        @Test
        public void testUntitled() {
            driver.getKeyboard().sendKeys("company");
            driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys("company");
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Login']")));
            driver.findElement(By.xpath("//*[@text='Login']")).click();
            new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='Make Payment']")));
            driver.findElement(By.xpath("//*[@text='Make Payment']")).click();
            driver.findElement(By.xpath("//*[@text='Cancel']")).click();
            driver.findElement(By.xpath("//*[@text='Logout']")).click();
        }

        @AfterMethod
        public void tearDown() {
            driver.quit();
        }
    }


}
