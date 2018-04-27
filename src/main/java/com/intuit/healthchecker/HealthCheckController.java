package com.intuit.healthchecker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/t360wsendpoints/health")
public class HealthCheckController {

    private List<String> endpoints;
    private HealthCheckerService healthChecker;

    public HealthCheckController(@Value("${app.endpoints}") String[] endpoints, HealthCheckerService healthChecker) {
        this.endpoints = Arrays.asList(endpoints);
        this.healthChecker = healthChecker;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, Object>> check() {
        Map<String, Object> health = healthChecker.checkEndpoints(endpoints);

        return ResponseEntity.ok(health);
    }
}
