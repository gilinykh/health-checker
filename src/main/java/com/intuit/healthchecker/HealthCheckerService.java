package com.intuit.healthchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HealthCheckerService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckerService.class);

    private RESTfulHealthClient healthClient;

    public HealthCheckerService(RESTfulHealthClient healthClient) {
        this.healthClient = healthClient;
    }

    public Map<String, Object> checkEndpoints(List<String> endpoints) {
        Map<String, Object> result = new HashMap<>();

        if (endpoints == null || endpoints.isEmpty()) {
            log.warn("Endpoints pool is empty. Check configuration.");
            return result;
        }

        endpoints.forEach(endpoint -> {
            Map<String, Object> endpointHealth = (Map<String, Object>) healthClient.fetchHealth(endpoint);
            Map.Entry<String, Object> healthEntry = endpointHealth.entrySet().iterator().next();
            result.put(healthEntry.getKey(), healthEntry.getValue());
        });

        return result;
    }
}
