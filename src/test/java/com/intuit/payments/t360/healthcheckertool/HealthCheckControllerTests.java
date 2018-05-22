package com.intuit.payments.t360.healthcheckertool;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(EasyMockRunner.class)
public class HealthCheckControllerTests {

    @Mock
    private HealthCheckService checkerService;

    @TestSubject
    private HealthCheckController checkController = new HealthCheckController(new String[]{}, checkerService);

    @Test
    public void testControllerOperational() throws Exception {
        EasyMock.expect(checkerService.checkEndpoints(Collections.<String>emptyList()))
                .andReturn(Arrays.asList(new HealthResponse("txnv4services.payments.intuit.net", healthResponse("txnv4services.payments.intuit.net", health(true)))));

        EasyMock.replay(checkerService);

        ResponseEntity checked = checkController.check();
        assertEquals(HttpStatus.OK, checked.getStatusCode());
    }

    private Map<String, Object> health(boolean healthy) {
        Map<String, Object> health = new HashMap<>();
        health.put("healthy", healthy);
        health.put("message", "OK");
        return health;
    }

    private Map<String, Map<String, Object>> healthResponse(String serviceName, Map<String, Object> health) {
        Map<String, Map<String, Object>> healthResponse = new HashMap<>();
        healthResponse.put(serviceName, health);
        return healthResponse;
    }
}
