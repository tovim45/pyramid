package com.pyramid.tests.ui;

import com.pyramid.modules.ModelModules;
import com.pyramid.tests.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Sanity")
@Feature("Priority 1")
public class win2Test extends BaseTest {

    @Test(priority = 0, description = "INT-34479 - linkedin Verify Connection Name")
    @TmsLink("INT-34479")
    @Severity(SeverityLevel.BLOCKER)
    public void loginUI() {
        ModelModules modelModules = new ModelModules();
        modelModules.win2Login();
        modelModules.clickModel();
    }
}
