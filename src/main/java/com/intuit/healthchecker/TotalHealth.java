package com.intuit.healthchecker;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class TotalHealth {
    
    private List<ServiceHealth> servicesHealth;

    public TotalHealth(List<ServiceHealth> servicesHealth) {
        this.servicesHealth = servicesHealth;
    }

    @JsonValue
    public Map<String, ServiceHealth.Health> asMap() {
        return servicesHealth.stream().collect(toMap(ServiceHealth::getServiceName, ServiceHealth::getHealth));
    }

    ServiceHealth.Health serviceHealth(String serviceName) {
        return servicesHealth.stream()
                .filter(serviceHealth -> serviceHealth.getServiceName().equals(serviceName))
                .findFirst()
                .map(ServiceHealth::getHealth)
                .orElseThrow(() -> new IllegalArgumentException("No health-check for service: [" + serviceName + "]"));
    }
}
