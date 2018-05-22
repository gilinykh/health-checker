package com.intuit.payments.t360.healthcheckertool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/t360wsendpoints/health")
public class HealthCheckController {

    private List<String> endpoints;
    private HealthCheckService healthChecker;

    public HealthCheckController(@Value("${app.endpoints}") String[] endpoints, HealthCheckService healthChecker) {
        this.endpoints = Arrays.asList(endpoints);
        this.healthChecker = healthChecker;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Map<String, Object>>> check() throws IOException {
        Collection<HealthResponse> healthResponses = healthChecker.checkEndpoints(endpoints);

        return ResponseEntity.ok(HealthResponseCollectionRepresentation.from(healthResponses).asMap());
    }
}
