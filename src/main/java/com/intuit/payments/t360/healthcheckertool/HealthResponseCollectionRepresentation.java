package com.intuit.payments.t360.healthcheckertool;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HealthResponseCollectionRepresentation {

    private Map<String, Map<String, Object>> health;

    public HealthResponseCollectionRepresentation(Map<String, Map<String, Object>> healthResponses) {
        this.health = healthResponses;
    }

    public static HealthResponseCollectionRepresentation from(Collection<HealthResponse> healthResponses) {
        Map<String, Map<String, Object>> health = new HashMap<>();
        Map.Entry<String, Map<String, Object>> healthEntry;

        if (healthResponses != null && !healthResponses.isEmpty()) {
            for (HealthResponse healthResponse : healthResponses) {
                healthEntry = healthResponse.asMap().entrySet().iterator().next();
                health.put(healthEntry.getKey(), healthEntry.getValue());
            }
        }

        return new HealthResponseCollectionRepresentation(health);
    }

    public Map<String, Map<String, Object>> asMap() {
        return health;
    }
}
