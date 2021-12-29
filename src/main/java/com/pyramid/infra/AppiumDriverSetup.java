package com.pyramid.infra;

import static com.pyramid.infra.logger.AutomationLogger.info;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import java.util.Map;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AppiumDriverSetup implements com.pyramid.infra.WebDriverProvider {

    public DesiredCapabilities getCapabilities(Map<String, String> params, String appPath) {

        info("---------------Caps Params-----------------------------------------");
//      info("Caps Params");
        info("deviceName: " + params.get("deviceName"));
        info("app: " + appPath);
        info("udid: " + params.get("udid"));
        info("platform: " + params.get("platform"));
        info("appPackage: " + params.get("appPackage"));
        info("appActivity: " + params.get("appActivity"));
        info("browserName: " +params.get("browserName"));
        info("serverUrl: " + params.get("serverUrl"));
        info("---------------Caps Params-----------------------------------------");

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, params.get("deviceName"));
        caps.setCapability(MobileCapabilityType.APP, appPath);
        caps.setCapability(MobileCapabilityType.UDID, params.get("udid"));
        caps.setCapability(MobileCapabilityType.PLATFORM, params.get("platform"));
        caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, params.get("appPackage"));
        caps.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, params.get("appActivity"));
        caps.setCapability(MobileCapabilityType.BROWSER_NAME, params.get("browserName"));
    return caps;
    }
}
