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
        EasyMock.expect(restTemplate.getForEntity("http://url.com", String.class)).andReturn(ResponseEntity.ok().<String>build());

        EasyMock.replay(restTemplate);

        HealthResponse response = checkClient.fetchHealth("http://url.com");
        Map<String, Map<String, Object>> health = response.asMap();
        assertEquals(1, health.size());
        assertEquals("http://url.com", health.entrySet().iterator().next().getKey());
        assertEquals("OK", health.entrySet().iterator().next().getValue().get("message"));
        assertEquals("http://url.com", health.entrySet().iterator().next().getValue().get("url"));
    }

    @Test
    public void testPlainText() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("http://url.com", String.class)).andReturn(ResponseEntity.ok("health Ok"));

        EasyMock.replay(restTemplate);

        HealthResponse response = checkClient.fetchHealth("http://url.com");
        Map<String, Map<String, Object>> health = response.asMap();
        assertEquals(1, health.size());
        assertEquals("http://url.com", health.entrySet().iterator().next().getKey());
        assertEquals("health Ok", health.entrySet().iterator().next().getValue().get("message"));
        assertEquals("http://url.com", health.entrySet().iterator().next().getValue().get("url"));
    }

    @Test
    public void testJson() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("http://url.com", String.class)).andReturn(ResponseEntity.ok("{\"service1\":{\"healthy\":true,\"message\":\"OK\"}}"));

        EasyMock.replay(restTemplate);

        HealthResponse response = checkClient.fetchHealth("http://url.com");
        Map<String, Map<String, Object>> health = response.asMap();
        assertEquals(1, health.size());
        assertEquals("service1", health.entrySet().iterator().next().getKey());
        assertEquals(3, health.entrySet().iterator().next().getValue().size());
        assertEquals(true, health.entrySet().iterator().next().getValue().get("healthy"));
        assertEquals("OK", health.entrySet().iterator().next().getValue().get("message"));
        assertEquals("http://url.com", health.entrySet().iterator().next().getValue().get("url"));
    }

    @Test
    public void testError() throws Exception {
        EasyMock.expect(restTemplate.getForEntity("http://url.com", String.class)).andReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<String>build());

        EasyMock.replay(restTemplate);

        HealthResponse response = checkClient.fetchHealth("http://url.com");
        Map<String, Map<String, Object>> health = response.asMap();
        assertEquals(1, health.size());
        assertEquals("http://url.com", health.entrySet().iterator().next().getKey());
        assertEquals("ERROR", health.entrySet().iterator().next().getValue().get("message"));
        assertEquals("http://url.com", health.entrySet().iterator().next().getValue().get("url"));
    }
}