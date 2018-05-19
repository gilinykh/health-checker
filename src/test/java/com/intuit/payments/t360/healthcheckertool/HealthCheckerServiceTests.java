package com.intuit.payments.t360.healthcheckertool;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(EasyMockRunner.class)
public class HealthCheckerServiceTests {

    private String[] endpoints;

    @Mock
    private HealthCheckClient healthClient;

    @TestSubject
    private HealthCheckService healthChecker = new HealthCheckService(healthClient);

    @Before
    public void setup() throws IOException {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application-test.yml"));
        endpoints = yaml.getObject().getProperty("app.endpoints").split(",");
    }

    @Test
    public void givenTwoEndpoints_whenCheckingHealth_thenCombinedHealthReturned() throws Exception {
        String endpoint1 = endpoints[0];
        String endpoint2 = endpoints[1];
        Map response1 = healthResponse("txnv4services.payments.intuit.net", health(true));
        Map response2 = healthResponse("lookup.ihub.payments.intuit.com/v2", health(false));

        EasyMock.expect(healthClient.fetchHealth(endpoint1)).andReturn(response1);
        EasyMock.expect(healthClient.fetchHealth(endpoint2)).andReturn(response2);

        EasyMock.replay(healthClient);

        Map checkedHealth = healthChecker.checkEndpoints(asList(endpoint1, endpoint2));
        assertEquals(true, ((Map) checkedHealth.get("txnv4services.payments.intuit.net")).get("healthy"));
        assertEquals(false, ((Map) checkedHealth.get("lookup.ihub.payments.intuit.com/v2")).get("healthy"));
    }

    @Test
    public void givenZeroEndpoints_whenCheckingHealth_thenEmptyResult() throws Exception {
        Map checkedHealth = healthChecker.checkEndpoints(new ArrayList<String>());
        assertTrue(checkedHealth.isEmpty());
    }

    private Map health(boolean healthy) {
        Map health = new HashMap();

        health.put("healthy", healthy);
        health.put("message", "OK");

        return health;
    }

    private Map healthResponse(String serviceName, Map health) {
        Map healthResponse = new HashMap();

        healthResponse.put(serviceName, health);

        return healthResponse;
    }
}
