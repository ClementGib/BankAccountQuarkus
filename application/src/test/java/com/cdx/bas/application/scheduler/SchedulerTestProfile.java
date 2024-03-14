package com.cdx.bas.application.scheduler;


import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.HashMap;
import java.util.Map;

public class SchedulerTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        Map<String, String> overridenConfig = new HashMap<>();
        overridenConfig.put("scheduler.activation", "true");
        overridenConfig.put("scheduler.every", "10m");
        return overridenConfig;
    }

}
