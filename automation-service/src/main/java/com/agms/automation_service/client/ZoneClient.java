package com.agms.automation_service.client;

import com.agms.automation_service.dto.ZoneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zone-service")   // Resolved via Eureka — no URL needed
public interface ZoneClient {

    @GetMapping("/api/zones/{id}")
    ZoneResponse getZoneById(@PathVariable("id") Long id);
}
