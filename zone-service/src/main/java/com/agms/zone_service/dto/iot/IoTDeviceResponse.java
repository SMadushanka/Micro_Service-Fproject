package com.agms.zone_service.dto.iot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IoTDeviceResponse {
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private String location;
    private String status;
}
