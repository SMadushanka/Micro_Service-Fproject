package com.agms.crop_service.service;

import com.agms.crop_service.dto.CropRequest;
import com.agms.crop_service.dto.CropResponse;
import com.agms.crop_service.model.Crop;
import com.agms.crop_service.model.CropStatus;
import com.agms.crop_service.repository.CropRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CropService {

    private final CropRepository cropRepository;

    @Transactional
    public CropResponse createCrop(CropRequest request) {
        log.info("Creating crop: {}", request.getName());
        Crop crop = Crop.builder()
                .name(request.getName())
                .quantity(request.getQuantity())
                .zoneId(request.getZoneId())
                .status(CropStatus.SEEDLING)   // All crops start as SEEDLING
                .build();
        return toResponse(cropRepository.save(crop));
    }

    @Transactional
    public CropResponse updateStatus(Long id, CropStatus newStatus) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found with id: " + id));

        CropStatus current = crop.getStatus();

        if (!current.canTransitionTo(newStatus)) {
            throw new IllegalArgumentException(
                    "Invalid state transition: " + current + " → " + newStatus
                    + ". Allowed transitions: SEEDLING → VEGETATIVE → HARVESTED");
        }

        log.info("Updating crop {} status: {} → {}", id, current, newStatus);
        crop.setStatus(newStatus);
        return toResponse(cropRepository.save(crop));
    }

    public List<CropResponse> getAllCrops() {
        return cropRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<CropResponse> getCropsByZone(Long zoneId) {
        return cropRepository.findByZoneId(zoneId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CropResponse getCropById(Long id) {
        return toResponse(cropRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crop not found with id: " + id)));
    }

    @Transactional
    public void deleteCrop(Long id) {
        if (!cropRepository.existsById(id)) {
            throw new RuntimeException("Crop not found with id: " + id);
        }
        cropRepository.deleteById(id);
        log.info("Deleted crop with id: {}", id);
    }

    private CropResponse toResponse(Crop crop) {
        return CropResponse.builder()
                .id(crop.getId())
                .name(crop.getName())
                .quantity(crop.getQuantity())
                .zoneId(crop.getZoneId())
                .status(crop.getStatus())
                .build();
    }
}
