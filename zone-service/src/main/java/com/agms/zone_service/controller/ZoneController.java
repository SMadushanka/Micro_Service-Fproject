package com.agms.zone_service.controller;

import com.agms.zone_service.dto.ZoneRequest;
import com.agms.zone_service.dto.ZoneResponse;
import com.agms.zone_service.dto.ZoneUpdateRequest;
import com.agms.zone_service.dto.iot.IoTDeviceResponse;
import com.agms.zone_service.dto.iot.IoTRefreshRequest;
import com.agms.zone_service.dto.iot.IoTRefreshResponse;
import com.agms.zone_service.service.IoTIntegrationService;
import com.agms.zone_service.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;
    private final IoTIntegrationService iotIntegrationService;

    @PostMapping
    public ResponseEntity<ZoneResponse> createZone(@Valid @RequestBody ZoneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(zoneService.createZone(request));
    }

    @GetMapping
    public ResponseEntity<List<ZoneResponse>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAllZones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getZoneById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ZoneResponse> updateZone(
            @PathVariable Long id,
            @RequestBody ZoneUpdateRequest request) {
        return ResponseEntity.ok(zoneService.updateZone(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        zoneService.deleteZone(id);
        return ResponseEntity.noContent().build();
    }

    // ── IoT proxy endpoints ────────────────────────────────────────────────

    /**
     * GET /api/zones/iot/devices
     * Lists all IoT devices for the authenticated user.
     * Pass the IoT Bearer token via the Authorization header.
     */
    @GetMapping("/iot/devices")
    public ResponseEntity<List<IoTDeviceResponse>> getIoTDevices(
            @RequestHeader("X-IoT-Token") String iotToken) {
        return ResponseEntity.ok(iotIntegrationService.getDevices(iotToken));
    }

    /**
     * POST /api/zones/iot/refresh
     * Refresh an expired IoT access token.
     * Body: { "refreshToken": "<refresh-token-value>" }
     */
    @PostMapping("/iot/refresh")
    public ResponseEntity<IoTRefreshResponse> refreshIoTToken(
            @RequestBody IoTRefreshRequest request) {
        return ResponseEntity.ok(iotIntegrationService.refreshToken(request.getRefreshToken()));
    }
}
