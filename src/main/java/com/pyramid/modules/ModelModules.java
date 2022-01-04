package com.pyramid.modules;

import com.pyramid.tests.BaseTest;
import org.openqa.selenium.By;

public class ModelModules extends BaseModule {

    private static final String URL = BaseTest.property.getProperty("win2Url");
    private static final String USER = BaseTest.property.getProperty("userwin2");
    private static final String PASS = BaseTest.property.getProperty("passwin2");
    private static final String TITLE = "Pyramid";

    private final By username = By.id("presented-username");
    private final By password = By.id("password");
    private final By login = By.id("btnSubmit");
    private final By model = By.id("side-menu-button-model");
    private final By uploadFile =  By.className("file-uploader");
    private final By buildBtn = By.id("model-save-btn");

    public ModelModules() {
        super(URL, TITLE);
        navigate();
    }

    public void win2Login() {
        sendKeys(username, USER);
        sendKeys(password, PASS);
        click(login);
    }

    public void clickModel() {
        click(model);
    }

    public void uploadFile(String fileName) {
        click(uploadFile);
        fileUpload(fileName);
    }

    public void clickBuild(){
        click(buildBtn);
    }
}
