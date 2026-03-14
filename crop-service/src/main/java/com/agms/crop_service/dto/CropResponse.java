package com.agms.crop_service.dto;

import com.agms.crop_service.model.CropStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CropResponse {
    private Long id;
    private String name;
    private Integer quantity;
    private Long zoneId;
    private CropStatus status;
}
