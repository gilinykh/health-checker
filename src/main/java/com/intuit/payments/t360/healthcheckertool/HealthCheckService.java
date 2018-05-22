package com.intuit.payments.t360.healthcheckertool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class HealthCheckService {

    private static final Logger log = LoggerFactory.getLogger(HealthCheckService.class);

    private HealthCheckClient healthClient;

    public HealthCheckService(HealthCheckClient healthClient) {
        this.healthClient = healthClient;
    }

    public Collection<HealthResponse> checkEndpoints(List<String> endpoints) throws IOException {
        Collection<HealthResponse> result = new ArrayList<>();

        if (endpoints == null || endpoints.isEmpty()) {
            log.warn("Endpoints pool is empty. Check configuration.");
            return result;
        }

        for (String endpoint : endpoints) {
            result.add(healthClient.fetchHealth(endpoint));
        }

        return result;
    }
}
