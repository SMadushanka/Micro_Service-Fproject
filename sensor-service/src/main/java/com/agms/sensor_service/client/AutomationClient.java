package com.agms.sensor_service.client;

import com.agms.sensor_service.dto.AutomationProcessRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "automation-service")   // Resolved via Eureka — no URL needed
public interface AutomationClient {

    /**
     * Forwards temperature reading to automation-service for rule evaluation.
     * Returns void — sensor-service does not need the action result.
     */
    @PostMapping("/api/automation/process")
    void process(@RequestBody AutomationProcessRequest request);
}
