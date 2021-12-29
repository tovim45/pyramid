package com.pyramid.tests.ui;

import static org.assertj.core.api.Assertions.assertThat;

import com.pyramid.modules.LinkedinModules;
import com.pyramid.tests.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.TmsLink;
import org.testng.annotations.Test;

@Epic("Sanity")
@Feature("Priority 1")
public class LinkedinTest extends BaseTest {

  @Test(priority = 0, description = "INT-34479 - linkedin Verify Connection Name")
  @TmsLink("INT-34479")
  @Severity(SeverityLevel.BLOCKER)
  public void linkedinVerifyConnectionNameUI() {
    LinkedinModules linkedinModules = new LinkedinModules();
    linkedinModules.linkedinLogin();
    linkedinModules.clickMe();
    linkedinModules.clickProfile();
    linkedinModules.clickConnections();
    assertThat(linkedinModules.linkedinVerifyConnectionName("Maia")).isTrue();
    assertThat(linkedinModules.linkedinVerifyConnectionName("nameThatIsNotExists")).isFalse();
  }
}
