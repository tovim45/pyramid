package com.pyramid.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {

    private static final String userBasicOperation = "src/main/java/com/pyramid/utils/apiClient/jsonFiles/basicOperationPerUser.json";

    public static JSONObject getUserProperties(String userName) {
        JSONParser parser = new JSONParser();
        Object obj = null;

        {
            try {
                obj = parser.parse(new FileReader(userBasicOperation));
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        JSONObject jsonObject = (JSONObject) obj;
        return (JSONObject) jsonObject.get(userName);
    }

    public static JSONArray getUserPageProperties(String userName, String page) {
        JSONObject userProp = getUserProperties(userName);
        return (JSONArray) userProp.get(page);
    }
}
