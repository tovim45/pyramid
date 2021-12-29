package com.pyramid.utils.apiClient;

import static com.pyramid.infra.logger.AutomationLogger.info;
import static com.pyramid.infra.logger.AutomationLogger.debug;

import com.aventstack.extentreports.ExtentTest;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import static com.pyramid.utils.Assertions.assertTextContains;
import static com.pyramid.utils.Assertions.assertTextExclude;
import static com.pyramid.utils.DataDrivenUtils.getListFrromString;
import static com.pyramid.utils.DataDrivenUtils.getMapFromStr;
import static com.pyramid.utils.apiClient.BaseDDTApi.getRequestOptions;
import static com.pyramid.utils.apiClient.BaseDDTApi.getRequestWithHeaders;
import static com.pyramid.utils.apiClient.BaseDDTApi.getRequestWithHeadersNoExpected;


public class DataDrivenApi {

  private HashMap headersMap;
  private JsonObject response;
  private HashMap<String, String> expectedResultMap;
  private String confirmationCode;
  private String user;
  private boolean isBearer = true; //flag to define getToken type (with 'Bearer' or not)

  /**
   * @param isBearer
   */
  public DataDrivenApi(boolean isBearer) {
    this.isBearer = isBearer;
  }

  public void startProsess(String accessToken, String accGrantType, String accSSOURL, String uri,
      String requestMethod, String headers, String headersForGetToken, String body,
      String expectedStatusCode, String expectedResult, String verifyList)
      throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, JSONException {
    headersMap = getMapFromStr(headers);
    getToken(accessToken, accGrantType, accSSOURL, headersForGetToken);
    if (confirmationCode != null) {
      body = addConfirmationCode(body);
    }
    response = getRequestWithHeaders(uri, requestMethod, body, headersMap,
        Integer.parseInt(expectedStatusCode));
    info("response is: " + response);
    validateResult(expectedResult, verifyList);
  }

  private void validateResult(String expectedResult, String verifyList) {
    if (response != null) {
      info("Response is:\n" + response.toString());
    }
    if (verifyList != null) {
      for (String param : getListFrromString(verifyList)) {
        assertTextContains(param, response.toString());
        info("Result as expected: " + verifyList);
      }
    }
    if (expectedResult != null) {
      expectedResultMap = getMapFromStr(expectedResult);
      for (String key : expectedResultMap.keySet()) {
        if (response.has(key)) {
          assertTextContains(expectedResultMap.get(key), response.get(key).toString());
          info("Result as expected: " + response.get(key).toString());
        } else {
          Assert.fail("Key: '" + key + "'  Is not appear in response");
        }
      }
    }
  }

  private void validateExcludeResult(String expectedResult, String verifyList,
      String verifyExcludeList) {
    if (response != null) {
      info("Response is:\n" + response.toString());
    }
    if (verifyList != null) {
      for (String param : getListFrromString(verifyList)) {
        assertTextContains(param, response.toString());
      }
    }
    if (expectedResult != null) {
      expectedResultMap = getMapFromStr(expectedResult);
      for (String key : expectedResultMap.keySet()) {
        if (response.has(key)) {
          assertTextContains(expectedResultMap.get(key), response.get(key).toString());
          info("Result as expected: " + response.get(key).toString());
        } else {
          Assert.fail("Key: '" + key + "'  Is not appear in response");
        }
      }
    }
    if (verifyExcludeList != null && verifyExcludeList != "") {
      for (String param : getListFrromString(verifyExcludeList)) {
        assertTextExclude(param, response.toString());
      }
    }
  }

  public String getValue(JsonObject response, String key) {
    String respValue = null;
    if (response != null) {
      respValue = response.get("data").getAsJsonArray().get(0).getAsJsonObject().get(key)
          .toString();
      info("Result as expected: " + respValue);
    } else {
      Assert.fail("Key: '" + key + "'  Is not appear in response");
    }
    return respValue.substring(1, 37);
  }

  private String getToken(String accessToken, String accGrantType, String accSSOURL,
      String headersForGetToken)
      throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
    if (accessToken.equals("true")) {
      response = getRequestWithHeaders(accSSOURL, "post", accGrantType,
          getMapFromStr(headersForGetToken), 200);
      if (isBearer) {
        headersMap.put("Authorization", "Bearer " + response.get("access_token").getAsString());
      } else {
        headersMap.put("Authorization", response.get("access_token").getAsString());
      }
      info("Access Token: " + response.get("access_token").getAsString());
    }
    return accessToken;
  }

  private String addConfirmationCode(String body) {
    if (body.contains("\"username\":")) {
      return body;
    }
    body = body.substring(0, body.length() - 2);
    body = body + "\"code\":\"" + confirmationCode + "\",\"username\":\"" + user + "\"}";
    info("Confirmation Code: " + confirmationCode);
    info(body);
    return body;
  }

  private String addParamToURI(String uri, String paramName, String Id) {
    uri = uri + "/" + paramName + "/" + Id;
    return uri;
  }

  public void setConfirmationCode(String confirmationCode) {
    this.confirmationCode = confirmationCode;
  }

  public void setUser(String user) {
    this.user = user;
  }

  private static String dataFile = java.nio.file.Paths.get(
      System.getProperty("user.dir"), "src", "test", "resources").toString() + File.separator;
}
