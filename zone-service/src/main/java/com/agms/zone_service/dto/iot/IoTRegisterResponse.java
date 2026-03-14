package com.agms.zone_service.dto.iot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IoTRegisterResponse {
    private String userId;
    private String username;
    private String email;
    private String message;
}
