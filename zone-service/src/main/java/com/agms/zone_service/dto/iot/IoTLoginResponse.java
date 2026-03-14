package com.agms.zone_service.dto.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IoTLoginResponse {
    private String token;
    private String tokenType;
    private Long expiresIn;
}
