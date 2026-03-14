package com.agms.zone_service.dto.iot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IoTRefreshResponse {
    private String token;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
}
