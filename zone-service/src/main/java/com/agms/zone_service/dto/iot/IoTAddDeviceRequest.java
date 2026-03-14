package com.agms.zone_service.dto.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IoTAddDeviceRequest {
    private String deviceName;
    private String deviceType;
    private String location;
}
