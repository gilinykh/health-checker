package com.intuit.healthchecker;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class RESTfulHealthClient {

    private RestTemplate restTemplate;

    public RESTfulHealthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map fetchHealth(String url) {
        return restTemplate.getForObject(url, Map.class);
    }

}
