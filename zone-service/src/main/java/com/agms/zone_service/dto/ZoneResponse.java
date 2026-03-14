package com.agms.zone_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponse {
    private Long id;
    private String name;
    private String description;
    private String deviceId;       // Returned from IoT API after device registration
    private String iotUserId;      // User registered in the IoT platform
    private Double minTemp;
    private Double maxTemp;
}
