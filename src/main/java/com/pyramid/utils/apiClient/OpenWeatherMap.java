package com.pyramid.utils.apiClient;

import static com.pyramid.infra.logger.AutomationLogger.info;

import com.google.gson.JsonObject;
import java.io.IOException;

public class OpenWeatherMap extends BaseApi {

  public JsonObject openWeatherMap() throws IOException {

    url = "http://api.openweathermap.org/data/2.5/weather?q=herzliya,IL&APPID=c9d95fe4183fb8ae56cd31325b70c168&units=metric";
    JsonObject response = getRequest(200);
    return response;
  }

  public void openWeatherMapGetTemp() throws IOException {
    url = "http://api.openweathermap.org/data/2.5/weather?q=herzliya,IL&APPID=c9d95fe4183fb8ae56cd31325b70c168&units=metric";
    JsonObject response = getRequest(200);

    String country = String.valueOf(response.getAsJsonObject("sys").get("country"));
    info("Country is " + country);

    String tmp = String.valueOf(response.getAsJsonObject("main").get("temp"));
    info("The temperature is " + tmp);
  }
}
