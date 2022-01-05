package com.pyramid.tests.ui;

import com.pyramid.modules.LoginModules;
import com.pyramid.modules.ModelModules;
import com.pyramid.tests.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.pyramid.utils.TestDataParser.loadTestDataAsMapStringString;

@Epic("Sanity")
@Feature("Priority 1")
public class win2Test extends BaseTest {

    private final File modelPath = new File("src/main/resources/test-data/api/modelData.yaml");

    @Test(priority = 0, description = "INT-34479 - Add new Model")
    @TmsLink("INT-34479")
    @Severity(SeverityLevel.BLOCKER)
    public void addModelUI() {
        String fileNameToUpload = loadTestDataAsMapStringString(modelPath).get("fileNameToUpload");
        String fileName = loadTestDataAsMapStringString(modelPath).get("Beer_Data");
        ModelModules modelModules = new ModelModules();
        LoginModules loginModules = new LoginModules();
        loginModules.win2Login();
        modelModules.clickModel();
        modelModules.uploadFile(fileNameToUpload);
        SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy_HHmmss");
        Date date = new Date();
        modelModules.setModelName(fileName + formatter.format(date));
        modelModules.clickBuild();
    }
}
