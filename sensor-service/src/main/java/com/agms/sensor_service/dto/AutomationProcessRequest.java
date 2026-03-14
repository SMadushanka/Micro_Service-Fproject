package com.agms.sensor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Matches automation-service's ProcessRequest exactly.
 * Sent to POST /api/automation/process.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutomationProcessRequest {
    private Long zoneId;
    private Double currentTemp;
}
