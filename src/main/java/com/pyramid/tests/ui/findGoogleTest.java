package com.pyramid.tests.ui;

import com.pyramid.modules.GoogleModule;
import com.pyramid.tests.BaseTest;
import org.testng.annotations.Test;

public class findGoogleTest extends BaseTest {

  @Test(priority = 0, testName = "googleFindTest", description = "google Find Test")
  public void googleFindTestUI() {
    GoogleModule googleModule = new GoogleModule();
    googleModule.findTextGoogle("Giora Tovim");


  }


}
