package com.pyramid.tests.android.examples;

import static java.lang.Thread.sleep;

import com.sun.jna.Platform;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumFacebook {

    public static void main(String[] args) {


        // Create object of  DesiredCapabilities class and specify android platform
        DesiredCapabilities capabilities = DesiredCapabilities.android();


        // set the capability to execute test in chrome browser
        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);

        // set the capability to execute our test in Android Platform
        capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);

        // we need to define platform name
        capabilities.setCapability(MobileCapabilityType.PLATFORM, "Android");

        // Set the device name as well (you can give any name)
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Carbon Mobile 5");

        // set the android version as well
        capabilities.setCapability(MobileCapabilityType.VERSION, "8.1.0");

        capabilities.setCapability(MobileCapabilityType.UDID, "401-686-709");


        // Create object of URL class and specify the appium server address
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Create object of  AndroidDriver class and pass the url and capability that we created
        WebDriver driver = new AndroidDriver<>(url, capabilities);

        // Open url
//	 driver.navigate().to("http://google.com");
        driver.get("http://www.facebook.com");

        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // print the title
        System.out.println("Title " + driver.getTitle());

        // enter username
        driver.findElement(By.name("email")).sendKeys("appium.test@gmail.com");


    }
}
