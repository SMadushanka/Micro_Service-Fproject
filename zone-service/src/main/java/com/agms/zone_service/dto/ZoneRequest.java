package com.agms.zone_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.agms.zone_service.validation.ValidTempRange;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidTempRange
public class ZoneRequest {

    @NotBlank(message = "Zone name is required")
    private String name;

    private String description;

    // IoT credentials — used to register/login and provision a device for this zone
    @NotBlank(message = "IoT username is required")
    private String iotUsername;

    @NotBlank(message = "IoT password is required")
    private String iotPassword;

    @NotBlank(message = "Device name is required")
    private String deviceName;

    @NotBlank(message = "Device type is required")
    private String deviceType;

    private Double minTemp;   // Minimum temperature threshold
    private Double maxTemp;   // Maximum temperature threshold
}
