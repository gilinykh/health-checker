package com.intuit.payments.t360.healthcheckertool;

import java.util.HashMap;
import java.util.Map;

public class HealthResponse {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";

    private Map<String, Map<String, Object>> health;

    public HealthResponse(String url, Map<String, Map<String, Object>> health) {
        if (health == null || health.isEmpty()) {
            throw new IllegalArgumentException("Health response is empty!");
        }
        this.health = new HashMap<>(health);
        this.health.entrySet().iterator().next().getValue().put("url", url);
    }

    Map<String, Map<String, Object>> asMap() {
        return health;
    }

    static HealthResponse asError(String url) {
        return new HealthResponse(url, mappedMessage(url, ERROR));
    }

    static HealthResponse asOk(String url) {
        return new HealthResponse(url, mappedMessage(url, OK));
    }

    static HealthResponse fromText(String responseText, String url) {
        return new HealthResponse(url, mappedMessage(url, responseText));
    }

    private static Map<String, Map<String, Object>> mappedMessage(String url, String message) {
        Map<String, Map<String, Object>> result = new HashMap<>();
        Map<String, Object> submap = new HashMap<>();
        result.put(url, submap);
        submap.put("message", message);
        return result;
    }
}
