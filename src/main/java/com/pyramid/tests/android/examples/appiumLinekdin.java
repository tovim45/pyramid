package com.pyramid.tests.android.examples;

//package <set your test package>;

import com.aventstack.extentreports.ExtentTest;
import com.pyramid.tests.BaseTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;


public class appiumLinekdin extends BaseTest {
    private String reportDirectory = "reports";
    private String reportFormat = "xml";
    private String testName = "Untitled";
    //    public AndroidDriver driver;
    private XmlTest testngXml;
    public static ExtentTest testLog;

    public String env = "Appium";
    public String portal = "Appium";


    @Test(priority = 0,testName = "demo")
    public void loginTestMobile() {

//        LinkedinPage linkedin = new LinkedinPage(driver);
//        linkedin.typeUsername("gilnada007@gmail.com");
//        linkedin.typePassword("gtgtgt");
//        linkedin.clickLogin();
        driver.get("https://www.amazon.com");




    }


}
