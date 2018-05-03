package com.intuit.healthchecker;

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
public class RESTfulHealthClient {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    public RESTfulHealthClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch health response as JSON from the provided endpoint.
     * NOTE: if the response is plain-text (JSON cannot be parsed); return in format {<endpoint_url>:<health text response>}
     * @param url Health endpoint.
     * @return JSON representation of health response.
     * @throws IOException in case of any IO problems.
     */
    public Map<String, Object> fetchHealth(String url) throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Map<String, Object> result;

        try {
            result = objectMapper.readValue(response.getBody(), new TypeReference<Map>(){});
        } catch (JsonParseException e) {
            result = new HashMap<>();
            result.put(url, response.getBody());
        }

        return result;
    }

}
