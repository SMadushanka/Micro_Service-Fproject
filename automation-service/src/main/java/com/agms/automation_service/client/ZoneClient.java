package com.agms.automation_service.client;

import com.agms.automation_service.dto.ZoneResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "zone-service")   // Resolved via Eureka — no URL needed
public interface ZoneClient {

    @GetMapping("/api/zones/{id}")
    ZoneResponse getZoneById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @PathVariable("id") Long id
    );
}
