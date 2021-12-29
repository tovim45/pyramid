package com.pyramid.tests.api.examples;

import com.pyramid.tests.BaseTest;
import com.pyramid.utils.DataDrivenUtils;
import com.pyramid.utils.apiClient.DataDrivenApi;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

class GioraApiDdt extends BaseTest {

  private String file;

  @BeforeSuite
  private void getFile() {
    file = ("src/main/resources/test-data/api/apiData.xls");
  }

  @DataProvider(name = "getApiGiora")
  public Object[][] getApiGiora() throws Exception {
    Object[][] arrayObject = DataDrivenUtils.getExcelData(file, "getApiGiora");
    return arrayObject;
  }

  @Test(dataProvider = "getApiGiora", groups = "ApiTests")

  public void getApiGioraDDT(String accessToken, String accGrantType, String accSSOURL, String uri,
      String requestMethod,
      String headers, String headersForGetToken, String body, String expectedStatusCode,
      String expectedResult, String verifyList, String comments, String rowNum) throws Exception {
    DataDrivenApi api = new DataDrivenApi(Boolean.FALSE);
    api.startProsess(accessToken, accGrantType, accSSOURL, uri, requestMethod, headers,
        headersForGetToken, body,
        expectedStatusCode, expectedResult, verifyList);
  }
}
