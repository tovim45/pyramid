package com.pyramid.utils.apiClient;

import static com.pyramid.infra.logger.AutomationLogger.info;

import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static com.pyramid.utils.apiClient.MySSLSocketFactory.getNewHttpClient;

public abstract class BaseApi {

  protected String url;
  private Gson gson = new GsonBuilder().create();
  protected HashMap<String, String> baseHeaders = new HashMap<>();
  protected String baseApiPath = java.nio.file.Paths.get(
      System.getProperty("user.dir"),
      "src", "main", "java", "com", "pyramid", "utils", "apiClient").toString() + File.separator;

  protected JsonObject getToken(String tokenUrl, String username, String password)
      throws IOException, JSONException {

    JSONObject obj = new JSONObject();
    obj.put("username", username);
    obj.put("password", password);

    url = tokenUrl;
    JsonObject response = getPost(obj, 200);
    return response;
  }

  protected JsonObject getTokenFromJsonFile(String tokenUrl, String username, String password,
      boolean isPayloadFromJsonFile) throws IOException, JSONException {

    JsonObject obj = readJsonFile("src/main/java/com/elbitsystems/utils/apiClient/auth.json");

    url = tokenUrl;
    JsonObject response = getPost(obj, 200);
    return response;
  }


  protected JsonObject postJsonFile(int expectedCode, String jsonFilePath, String jsonOutFile)
      throws IOException, JSONException, ParseException {
    //Get the time
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd/HHmmss");
    LocalDateTime now = LocalDateTime.now();
    String externalReferenceValue = "A/" + dtf.format(now);

    try {
      String content = FileUtils.readFileToString(new File(jsonFilePath), "UTF-8");
//      info("Read json file before update text" + content);
      content = content.replaceAll("A/99999999/999999", externalReferenceValue);

//      info("Read json file after update text" + content);
      File tempFile = new File(jsonOutFile);
      FileUtils.writeStringToFile(tempFile, content, "UTF-8");
    } catch (IOException e) {
      throw new RuntimeException("Generating file failed", e);
    }

    JsonObject obj = readJsonFile(jsonOutFile);
    info("Read value after update json file" + String.valueOf(obj.get("external_references")));
    JsonObject response = getPost(obj, 200);
    info(String.valueOf(response.getAsJsonObject("general").get("entity_id")));
    return response;
  }


  protected JSONObject postSOAPXML(String requestData, int expectedCode)
      throws IOException, JSONException {
    HttpPost post = new HttpPost(url);
    post.setEntity(new StringEntity(requestData, "UTF-8"));
    baseHeaders.forEach(post::addHeader);
    String entity = executeRequest("<textarea>" + requestData + "</textarea>", expectedCode, post);
    return XML.toJSONObject(entity);

  }

  protected JsonObject getRequest(int expectedCode) throws IOException {
    HttpGet get = new HttpGet(url);
    baseHeaders.forEach(get::addHeader);
    String entity = executeRequest("", expectedCode, get);
    return gson.fromJson(entity, JsonObject.class);
  }

  protected JsonObject getRequestWithHeaders(HashMap<String, String> headers, int expectedCode)
      throws IOException {
    HttpGet get = new HttpGet(url);
    headers.forEach(get::addHeader);
    String entity = executeRequest("", expectedCode, get);
    return gson.fromJson(entity, JsonObject.class);
  }

  protected JsonObject getPost(JSONObject requestData, int expectedCode) throws IOException {
    HttpPost post = new HttpPost(url);
    baseHeaders.forEach(post::addHeader);
    post.setEntity(new StringEntity(String.valueOf(requestData), "UTF-8"));
    String entity = executeRequest(requestData.toString(), expectedCode, post);
    return gson.fromJson(entity, JsonObject.class);
  }

  protected JsonObject getPost(JsonObject requestData, int expectedCode) throws IOException {
    HttpPost post = new HttpPost(url);
    baseHeaders.forEach(post::addHeader);
    post.setEntity(new StringEntity(String.valueOf(requestData), "UTF-8"));
    String entity = executeRequest(requestData.toString(), expectedCode, post);
    return gson.fromJson(entity, JsonObject.class);
  }


  protected JsonObject getPost(String requestData, int expectedCode) throws IOException {
    HttpPost post = new HttpPost(url);
    baseHeaders.forEach(post::addHeader);
    post.setEntity(new StringEntity(requestData, "UTF-8"));
    String entity = executeRequest(requestData, expectedCode, post);
    return gson.fromJson(entity, JsonObject.class);
  }


  protected String getPostEntity(String requestData, int expectedCode) throws IOException {
    HttpPost post = new HttpPost(url);
    baseHeaders.forEach(post::addHeader);
    post.setEntity(new StringEntity(requestData, "UTF-8"));
    String entity = executeRequest(requestData, expectedCode, post);
    return entity;
  }

  protected JsonObject getPatch(String requestData, int expectedCode) throws IOException {
    HttpPatch patch = new HttpPatch(url);
    baseHeaders.forEach(patch::addHeader);
    patch.setEntity(new StringEntity(String.valueOf(requestData), "UTF-8"));
    String entity = executeRequest(requestData.toString(), expectedCode, patch);
    return gson.fromJson(entity, JsonObject.class);
  }

  protected JsonObject getDelete(int expectedCode) throws IOException {
    info("deleting....");
    HttpDelete del = new HttpDelete(url);
    baseHeaders.forEach(del::addHeader);
    String entity = executeRequest("", expectedCode, del);
    return gson.fromJson(entity, JsonObject.class);
  }


  private String executeRequest(String requestData, int expectedCode, HttpRequestBase request)
      throws IOException {
    HttpClient client = getNewHttpClient();
    long startTime = System.currentTimeMillis();
    HttpResponse response = client.execute(request);
    String entity = EntityUtils.toString(response.getEntity());
    reportRequestData(url, request.getMethod(), baseHeaders, startTime, requestData, entity);
    validateStatusCode(expectedCode, response, entity);
    return entity;
  }


  public static void reportRequestData(String url, String method, HashMap<String, String> headers,
      long startTime, String requestData, String entity) {
    info("Sending request to URL : " + url);
    info("Method: " + method);
    info("Headers: " + headers.toString());
    info("request data: " + requestData);
    info("Response Time: " + (System.currentTimeMillis() - startTime));
    info("Response Data: " + entity);
  }


  protected JsonObject getAtter(JsonObject response, String[] keys) {
    for (String k : keys) {
      try {
        response = response.getAsJsonObject(k);
      } catch (ClassCastException e) {
        JsonArray list = response.getAsJsonArray(k);
        response = (JsonObject) list.get(0);
      }
    }
    return response;
  }


  public static JsonObject readJsonFile(String filePath) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filePath));
    } catch (FileNotFoundException e1) {
      e1.printStackTrace();
    }
    Gson gson = new Gson();
    assert br != null;
    return gson.fromJson(br, JsonObject.class);
  }

  protected static Document readXMLFile(String filePath)
      throws IOException, ParserConfigurationException, SAXException {
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
    return docBuilder.parse(filePath);
  }

  protected String convertDocToStr(Document xmlFile) throws TransformerException {
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer trans = tf.newTransformer();
    StringWriter sw = new StringWriter();
    trans.transform(new DOMSource(xmlFile), new StreamResult(sw));
    return sw.toString();
  }


  private void validateStatusCode(int expectedCode, HttpResponse response, String entity) {
    int responseCode = response.getStatusLine().getStatusCode();
    if (responseCode != expectedCode) {
      info(entity);
      info("request failed:  " + entity);
      Assert.assertEquals(responseCode, expectedCode);
    }
  }

  protected String getPostReturnString(JsonObject requestData, int expectedCode)
      throws IOException {
    HttpPost post = new HttpPost(url);
    baseHeaders.forEach(post::addHeader);
    post.setEntity(new StringEntity(String.valueOf(requestData), "UTF-8"));
    String entity = executeRequest(requestData.toString(), expectedCode, post);
    return entity;
  }

  protected String getPostReturnString(String requestData, int expectedCode) throws IOException {
    HttpPost post = new HttpPost(url);
    baseHeaders.forEach(post::addHeader);
    post.setEntity(new StringEntity(String.valueOf(requestData), "UTF-8"));
    String entity = executeRequest(requestData.toString(), expectedCode, post);
    return entity;
  }
}
