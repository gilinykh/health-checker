package com.intuit.healthchecker;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RESTfulHealthClient {

    private RestTemplate restTemplate;

    public RESTfulHealthClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ServiceHealth fetchHealth(String url) {
        return restTemplate.getForObject(url, ServiceHealth.class);
    }
}
