package com.pyramid.tests.api.examples;

import com.aventstack.extentreports.ExtentTest;
import com.pyramid.tests.BaseTest;
import com.pyramid.utils.DataDrivenUtils;
import com.pyramid.utils.apiClient.DataDrivenApi;
import org.apache.commons.codec.digest.DigestUtils;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
//import us.monoid.web.Resty;

import static com.pyramid.infra.logger.AutomationLogger.info;
class marvelApiDdt extends BaseTest {

    private String file;
    String publicKey = "88e03a6c65fbbcac5fefe51f8bd897d5";
    String privateKey = "0da0c6bbaaa50fddf5cf5ed44376353bd7aae3e5";
    long timeStamp = System.currentTimeMillis();
    int limit = 5;
    String stringToHash = timeStamp + privateKey + publicKey;
    String hash = DigestUtils.md5Hex(stringToHash);

    @BeforeSuite
    private void getFile() { file = ("C:\\qtest\\automation6\\automation\\tests\\src\\main\\resources\\apiDataMarvel.xls");
    }

    @DataProvider(name = "getApiMarvel")
    public Object[][] getApiMarvel() throws Exception {
        Object[][] arrayObject = DataDrivenUtils.getExcelData(file, "Characters");
        return arrayObject;
    }

    @Test(dataProvider = "getApiMarvel", groups = "ApiTests")

    public void getApiMarvelDDT(String accessToken, String accGrantType, String accSSOURL, String uri, String param, String requestMethod,
                               String headers, String headersForGetToken, String body, String expectedStatusCode,
                               String expectedResult, String verifyList, String comments, String rowNum) throws Exception {
        info (rowNum + ". " + comments);
        uri = uri + param;
        String url = String.format(uri, timeStamp, publicKey, hash, limit);

        DataDrivenApi api = new DataDrivenApi(Boolean.FALSE);
        api.startProsess(accessToken, accGrantType, accSSOURL, url, requestMethod, headers, headersForGetToken, body,
                expectedStatusCode, expectedResult, verifyList);
    }

}
