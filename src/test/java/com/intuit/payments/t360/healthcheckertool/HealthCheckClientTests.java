package com.intuit.payments.t360.healthcheckertool;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(EasyMockRunner.class)
public class HealthCheckClientTests {

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @TestSubject
    private HealthCheckClient checkClient = new HealthCheckClient(restTemplate, objectMapper);

    @Test
    public void testEmptyBody() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("url", String.class)).andReturn(ResponseEntity.ok().<String>build());

        EasyMock.replay(restTemplate);

        Map<String, Object> health = checkClient.fetchHealth("url");
        assertEquals(1, health.size());
        assertEquals("url", health.entrySet().iterator().next().getKey());
        assertEquals("OK", health.entrySet().iterator().next().getValue());
    }

    @Test
    public void testPlainText() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("url", String.class)).andReturn(ResponseEntity.ok("health Ok"));

        EasyMock.replay(restTemplate);

        Map<String, Object> health = checkClient.fetchHealth("url");
        assertEquals(1, health.size());
        assertEquals("url", health.entrySet().iterator().next().getKey());
        assertEquals("health Ok", health.entrySet().iterator().next().getValue());
    }

    @Test
    public void testJson() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("url", String.class)).andReturn(ResponseEntity.ok("{\"service1\":{\"healthy\":true,\"message\":\"OK\"}}"));

        EasyMock.replay(restTemplate);

        Map<String, Object> health = checkClient.fetchHealth("url");
        assertEquals(1, health.size());
        assertEquals("service1", health.entrySet().iterator().next().getKey());
        assertEquals(2, ((Map) health.entrySet().iterator().next().getValue()).size());
    }

    @Test
    public void testError() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("url", String.class)).andReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<String>build());

        EasyMock.replay(restTemplate);

        Map<String, Object> health = checkClient.fetchHealth("url");
        assertEquals(1, health.size());
        assertEquals("url", health.entrySet().iterator().next().getKey());
        assertEquals("ERROR", health.entrySet().iterator().next().getValue());
    }
}