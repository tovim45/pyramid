package com.pyramid.modules;

import com.pyramid.tests.BaseTest;
import org.openqa.selenium.By;

public class ModelModules extends BaseModule {

    private static final String URL = BaseTest.property.getProperty("win2Url");
    private static final String TITLE = "Pyramid";

    private final By model = By.id("side-menu-button-model");
    private final By uploadFile =  By.className("file-uploader");
    private final By buildBtn = By.id("model-save-btn");
    private final By modelName = By.xpath("//input[@id='model-name-input']");

    public ModelModules() {
        super(URL, TITLE);
        navigate();
    }

    public void clickModel() {
        click(model);
    }

    public void uploadFile(String fileName) {
        click(uploadFile);
        fileUpload(fileName);
    }

    public void setModelName(String fileName) {
        sendKeys(modelName, fileName);
    }

    public void clickBuild(){
        click(buildBtn);
    }
}
