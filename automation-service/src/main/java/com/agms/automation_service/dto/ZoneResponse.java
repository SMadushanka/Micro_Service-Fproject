package com.agms.automation_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors the ZoneResponse from zone-service.
 * Only the fields needed by automation-service are declared;
 * unknown fields are ignored via @JsonIgnoreProperties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZoneResponse {
    private Long id;
    private String name;
    private Double minTemp;
    private Double maxTemp;
}
