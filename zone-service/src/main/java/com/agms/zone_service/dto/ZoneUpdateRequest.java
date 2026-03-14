package com.agms.zone_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for updating a zone's mutable fields.
 * IoT device/user provisioning is NOT re-triggered on update.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZoneUpdateRequest {

    private String name;
    private String description;
    private Double minTemp;
    private Double maxTemp;
}
