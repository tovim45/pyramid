package com.pyramid.utils.k8s;

import com.pyramid.tests.BaseTest;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1ContainerStatus;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.Config;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.pyramid.infra.logger.AutomationLogger.info;

public class kubernetes extends BaseTest {


    @Test(priority = 1, testName = "getPodStatus", description = "get all Pod Status")
    public void getPodStatus(String env) throws IOException, ApiException {
        String conigFlePath = java.nio.file.Paths.get(new File(System.getProperty("user.dir")).getParent(), "pyramid", "src", "main", "java", "com", "pyramid", "infra", "k8s", "config").toString();
        //        String conigFlePath = System.getenv("KUBECONFIG");
        System.out.println(conigFlePath);
        info(conigFlePath);
        ApiClient client;
        client = Config.fromConfig(conigFlePath);
        Configuration.setDefaultApiClient(client);
        CoreV1Api api = new CoreV1Api();
        V1PodList list = null;
        list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        for (V1Pod item : list.getItems()) {
            if (item.getMetadata().getNamespace().contains(env)) {
                List<V1ContainerStatus> con = item.getStatus().getContainerStatuses();
                Boolean podReady = con.get(0).getReady();
                int podRestartCount = con.get(0).getRestartCount();

                System.out.println(item.getMetadata().getNamespace() + " " + item.getMetadata().getName() +
                        " podRestartCount=" + podRestartCount + " isPodReady=" + podReady);
                info(item.getMetadata().getNamespace() + "  " + item.getMetadata().getName() +
                        " podRestartCount=" + podRestartCount + " IsPodReady=" + podReady);

                if (!podReady) {
                    info(String.valueOf(con));
                }
            }
        }
    }


    @Test(priority = 2, testName = "Intel360GetImages", description = "Intel360 Get Images ")
    public void getImages(String env) throws IOException, ApiException {
        String conigFlePath = java.nio.file.Paths.get(new File(System.getProperty("user.dir")).getParent(), "pyramid", "src", "main", "java", "com", "pyramid", "infra", "k8s", "config").toString();
        System.out.println(conigFlePath);
        info(conigFlePath);
        ApiClient client;
        client = Config.fromConfig(conigFlePath);
        Configuration.setDefaultApiClient(client);
        CoreV1Api api = new CoreV1Api();
        V1PodList list = null;
        list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null, null);

        for (V1Pod item : list.getItems()) {
            if (item.getMetadata().getNamespace().contains(env)) {
                List<V1ContainerStatus> con = item.getStatus().getContainerStatuses();
                info(item.getMetadata().getName() + ": " + con.get(0).getImage());
            }
        }
    }
}
