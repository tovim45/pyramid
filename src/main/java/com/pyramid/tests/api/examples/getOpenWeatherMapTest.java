package com.pyramid.tests.api.examples;

import com.pyramid.tests.BaseTest;
import com.pyramid.utils.apiClient.OpenWeatherMap;
import org.testng.annotations.Test;


public class getOpenWeatherMapTest extends BaseTest {

    @Test(priority = 0, testName = "getOpenWeatherMapTest", description = "Get the Weather from OpenWeatherMap ")
    public void getOpenWeatherMapAPI() throws Exception {
        OpenWeatherMap op = new OpenWeatherMap();
        op.openWeatherMap();
        op.openWeatherMapGetTemp();
    }
}
