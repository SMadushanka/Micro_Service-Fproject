package com.agms.sensor_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Represents telemetry data returned from the external IoT API.
 * Adjust field names to match your actual IoT API response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelemetryResponse {
    private String deviceId;
    private Double temperature;
    private Double humidity;
    private Double soilMoisture;
    private String status;
    private Instant timestamp;
}
