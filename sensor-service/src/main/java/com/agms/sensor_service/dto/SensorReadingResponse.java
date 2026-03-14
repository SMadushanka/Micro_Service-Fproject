package com.agms.sensor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Returned to callers of GET /api/sensors/latest.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReadingResponse {
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private Double soilMoisture;
    private String status;
    private Instant recordedAt;   // When the scheduler fetched this reading
}
