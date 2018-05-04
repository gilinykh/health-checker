package com.intuit.healthchecker;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@RunWith(EasyMockRunner.class)
public class HealthCheckControllerTests {

    @Mock
    private HealthCheckerService checkerService;

    @TestSubject
    private HealthCheckController checkController = new HealthCheckController(new String[] {}, checkerService);

    @Test
    public void testControllerOperational() throws Exception {
        EasyMock.expect(checkerService.checkEndpoints(Collections.emptyList())).andReturn(new HashMap<>());

        EasyMock.replay(checkerService);

        ResponseEntity checked = checkController.check();
        assertEquals(HttpStatus.OK, checked.getStatusCode());
    }
}
