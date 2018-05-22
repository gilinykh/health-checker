package com.intuit.payments.t360.healthcheckertool;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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
     * NOTE: for non-JSON responses health's "message" is assumed to be either plain-text body or calculated based on status.
     *
     * @param url Health endpoint.
     * @return JSON representation of health response.
     * @throws IOException in case of any IO problems.
     */
    public HealthResponse fetchHealth(String url) throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Map<String, Map<String, Object>> result;

        if (!response.getStatusCode().is2xxSuccessful()) {
            return HealthResponse.asError(url);
        }

        if (response.getBody() == null) {
            return HealthResponse.asOk(url);
        }

        try {
            result = objectMapper.readValue(response.getBody(), new TypeReference<Map>(){});
        } catch (JsonParseException e) {
            return HealthResponse.fromText(response.getBody(), url);
        }

        return new HealthResponse(url, result);
    }
}
