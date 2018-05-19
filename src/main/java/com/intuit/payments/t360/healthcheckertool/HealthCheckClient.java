package com.intuit.payments.t360.healthcheckertool;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class HealthCheckClient {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    public HealthCheckClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch health response as JSON from the provided endpoint.
     * NOTE: if the response is plain-text (JSON cannot be parsed); return in format {<endpoint_url>:<health text response>}
     *
     * @param url Health endpoint.
     * @return JSON representation of health response.
     * @throws IOException in case of any IO problems.
     */
    public Map<String, Object> fetchHealth(String url) throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return HealthResponse.fromResponseEntity(response, url, objectMapper).asMap();
    }

    static class HealthResponse {
        private static final String OK = "OK";
        private static final String ERROR = "ERROR";
        private Map<String, Object> health;

        HealthResponse(Map<String, Object> health) {
            this.health = health != null ? health : new HashMap<String, Object>();
        }

        Map<String, Object> asMap() {
            return health;
        }

        static HealthResponse fromResponseEntity(ResponseEntity<String> responseEntity, String url, ObjectMapper objectMapper) throws IOException {
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                return asError(url);
            }

            if (responseEntity.getBody() == null) {
                return asOk(url);
            }

            Map<String, Object> result;

            try {
                result = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map>() {});
            } catch (JsonParseException e) {
                return fromText(responseEntity.getBody(), url);
            }

            return new HealthResponse(result);
        }

        static HealthResponse asError(final String url) {
            return new HealthResponse(new HashMap() {{
                put(url, ERROR);
            }});
        }

        static HealthResponse asOk(final String url) {
            return new HealthResponse(new HashMap() {{
                put(url, OK);
            }});
        }

        static HealthResponse fromText(final String responseText, final String url) {
            return new HealthResponse(new HashMap() {{
                put(url, responseText);
            }});
        }
    }

}
