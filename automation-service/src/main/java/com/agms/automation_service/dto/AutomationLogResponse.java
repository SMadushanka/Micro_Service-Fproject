package com.agms.automation_service.dto;

import com.agms.automation_service.model.AutomationAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutomationLogResponse {
    private Long id;
    private Long zoneId;
    private Double temperature;
    private AutomationAction action;
    private Instant timestamp;
}
