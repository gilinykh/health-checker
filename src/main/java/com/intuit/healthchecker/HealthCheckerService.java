package com.intuit.healthchecker;

import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class HealthCheckerService {

    private RESTfulHealthClient healthClient;

    public HealthCheckerService(RESTfulHealthClient healthClient) {
        this.healthClient = healthClient;
    }

    public TotalHealth checkEndpoints(List<String> endpoints) {
        return new TotalHealth(endpoints.stream().map(healthClient::fetchHealth).collect(toList()));
    }
}
