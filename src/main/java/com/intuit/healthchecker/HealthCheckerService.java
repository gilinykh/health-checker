package com.intuit.healthchecker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HealthCheckerService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckerService.class);

    private HealthCheckClient healthClient;

    public HealthCheckerService(HealthCheckClient healthClient) {
        this.healthClient = healthClient;
    }

    public Map<String, Object> checkEndpoints(List<String> endpoints) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> endpointHealth;
        Map.Entry<String, Object> healthEntry;

        if (endpoints == null || endpoints.isEmpty()) {
            log.warn("Endpoints pool is empty. Check configuration.");
            return result;
        }

        for (String endpoint : endpoints) {
            endpointHealth = healthClient.fetchHealth(endpoint);
            healthEntry = endpointHealth.entrySet().iterator().next();
            result.put(healthEntry.getKey(), healthEntry.getValue());
        }

        return result;
    }
}
