package com.agms.sensor_service.controller;

import com.agms.sensor_service.dto.SensorReadingResponse;
import com.agms.sensor_service.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    /**
     * Returns the latest telemetry reading fetched by the scheduler.
     * Returns 204 No Content if no reading has been fetched yet.
     */
    @GetMapping("/latest")
    public ResponseEntity<SensorReadingResponse> getLatestReading() {
        SensorReadingResponse reading = sensorService.getLatestReading();
        if (reading == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reading);
    }
}
