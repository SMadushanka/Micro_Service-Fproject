package com.agms.sensor_service.client;

import com.agms.sensor_service.dto.TelemetryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "iot-telemetry-client", url = "${iot.api.base-url}")
public interface IoTTelemetryClient {

    /**
     * Fetch the latest telemetry for a specific device.
     * Adjust the path to match your actual IoT API endpoint.
     */
    @GetMapping("/devices/{deviceId}/telemetry/latest")
    TelemetryResponse getLatestTelemetry(
            @RequestHeader("Authorization") String bearerToken,
            @PathVariable("deviceId") String deviceId
    );
}
