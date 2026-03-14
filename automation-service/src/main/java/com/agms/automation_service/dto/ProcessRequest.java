package com.agms.automation_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRequest {

    @NotNull(message = "zoneId is required")
    private Long zoneId;

    @NotNull(message = "currentTemp is required")
    private Double currentTemp;
}
