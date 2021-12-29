package com.pyramid.tests.k8s;

import com.pyramid.tests.BaseTest;
import com.pyramid.utils.k8s.kubernetes;
import org.testng.annotations.Test;

public class getK8sData extends BaseTest {
    kubernetes k8s = new kubernetes();

    @Test(priority = 0, testName = "get Pods Status", description = "get all Pods Status", groups = {"k8s"})
    public void getallPodsStatus() throws Exception {
        System.out.println("Get all pods status in " + envName);
        k8s.getPodStatus(envName);

    }

    @Test(priority = 1, testName = "get images", description = "get all images", groups = {"k8s"})
    public void getallImages() throws Exception {
        System.out.println("Get all images in ");
        k8s.getImages(envName);
    }
}
