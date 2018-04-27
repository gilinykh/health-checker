package com.intuit.healthchecker;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@RunWith(EasyMockRunner.class)
public class HealthCheckerServiceTests {

    @Mock
    private RESTfulHealthClient healthClient;

    @TestSubject
    private HealthCheckerService healthChecker = new HealthCheckerService(healthClient);

    @Test
    public void givenTwoEndpoints_whenCheckingHealth_thenCombinedHealthReturned() throws Exception {
        String endpoint1 = "http://service1/health";
        String endpoint2 = "http://service2/health";
        ServiceHealth health1 = serviceHealth("service1", health(true, "OK"));
        ServiceHealth health2 = serviceHealth("service2", health(false, "DOWN"));

        EasyMock.expect(healthClient.fetchHealth(endpoint1)).andReturn(health1);
        EasyMock.expect(healthClient.fetchHealth(endpoint2)).andReturn(health2);

        EasyMock.replay(healthClient);

        TotalHealth checkedHealth = healthChecker.checkEndpoints(asList(endpoint1, endpoint2));
        assertEquals(true, checkedHealth.serviceHealth("service1").isHealthy());
        assertEquals(false, checkedHealth.serviceHealth("service2").isHealthy());
    }

    private ServiceHealth.Health health(boolean healthy, String message) {
        return new ServiceHealth.Health(healthy, message);
    }

    private ServiceHealth serviceHealth(String serviceName, ServiceHealth.Health health) {
        return new ServiceHealth(serviceName, health);
    }
}
