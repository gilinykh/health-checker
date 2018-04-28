package com.intuit.healthchecker;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@Component
public class RESTfulHealthClient {

    private RestTemplate restTemplate;

    private ObjectMapper objectMapper;

    public RESTfulHealthClient(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public Map fetchHealth(String url) throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return objectMapper.readValue(response.getBody(), new TypeReference<Map>(){});
    }

}
