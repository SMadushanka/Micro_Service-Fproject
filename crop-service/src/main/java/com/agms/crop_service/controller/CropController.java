package com.agms.crop_service.controller;

import com.agms.crop_service.dto.CropRequest;
import com.agms.crop_service.dto.CropResponse;
import com.agms.crop_service.model.CropStatus;
import com.agms.crop_service.service.CropService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@RequiredArgsConstructor
public class CropController {

    private final CropService cropService;

    /** POST /api/crops */
    @PostMapping
    public ResponseEntity<CropResponse> createCrop(@Valid @RequestBody CropRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cropService.createCrop(request));
    }

    /** GET /api/crops  or  GET /api/crops?zoneId=1 */
    @GetMapping
    public ResponseEntity<List<CropResponse>> getAllCrops(
            @RequestParam(required = false) Long zoneId) {
        if (zoneId != null) {
            return ResponseEntity.ok(cropService.getCropsByZone(zoneId));
        }
        return ResponseEntity.ok(cropService.getAllCrops());
    }

    /** GET /api/crops/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<CropResponse> getCropById(@PathVariable Long id) {
        return ResponseEntity.ok(cropService.getCropById(id));
    }

    /** PUT /api/crops/{id}/status?status=VEGETATIVE */
    @PutMapping("/{id}/status")
    public ResponseEntity<CropResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam CropStatus status) {
        return ResponseEntity.ok(cropService.updateStatus(id, status));
    }

    /** DELETE /api/crops/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return ResponseEntity.noContent().build();
    }
}
