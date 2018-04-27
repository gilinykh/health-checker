package com.intuit.healthchecker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

// explicit mapping of health response fields
public class ServiceHealth {

    private String serviceName;

    private Health health;

    public ServiceHealth(String serviceName, Health health) {
        this.serviceName = serviceName;
        this.health = health;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Health getHealth() {
        return health;
    }

    // expecting health to be the first (and only) entry in response
    // expected format: {"checkImage":{"healthy":true,"message":"OK"}}
    @JsonCreator
    public static ServiceHealth fromMap(Map<String, Health> serviceHealth) {
        if (serviceHealth == null || serviceHealth.isEmpty()) {
            throw new IllegalArgumentException("Empty health - check endpoint's response!");
        }

        Map.Entry<String, Health> healthEntry = serviceHealth.entrySet().iterator().next();

        return new ServiceHealth(healthEntry.getKey(), healthEntry.getValue());
    }

    @JsonValue
    public Map<String, Health> asMap() {
        Map<String, Health> result = new HashMap<>();
        result.put(serviceName, health);
        return result;
    }

    public static class Health {
        private boolean healthy;
        private String message;

        @JsonCreator
        public Health(@JsonProperty("healthy") boolean healthy, @JsonProperty("message") String message) {
            this.healthy = healthy;
            this.message = message;
        }

        public boolean isHealthy() {
            return healthy;
        }

        public String getMessage() {
            return message;
        }
    }
}
