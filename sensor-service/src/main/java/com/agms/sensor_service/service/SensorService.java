package com.agms.sensor_service.service;

import com.agms.sensor_service.dto.SensorReadingResponse;
import com.agms.sensor_service.dto.TelemetryResponse;
import com.agms.sensor_service.dto.AutomationProcessRequest;
import com.agms.sensor_service.client.IoTTelemetryClient;
import com.agms.sensor_service.client.AutomationClient;
import com.agms.sensor_service.model.SensorReading;
import com.agms.sensor_service.repository.SensorReadingRepository;
import com.agms.sensor_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorService {

    private final IoTTelemetryClient iotTelemetryClient;
    private final AutomationClient automationClient;
    private final TokenRefreshService tokenRefreshService;
    private final SensorReadingRepository readingRepository;
    private final JwtUtil jwtUtil;

    @Value("${iot.api.device-id}")
    private String deviceId;

    @Value("${iot.api.zone-id}")
    private Long zoneId;

    @Value("${agms.internal.service-subject:sensor-service}")
    private String internalServiceSubject;

    // Fast in-memory cache — backed by DB for persistence across restarts
    private final AtomicReference<SensorReadingResponse> latestCache = new AtomicReference<>();

    /**
     * Called by the scheduler every 10 seconds.
     * Fetches telemetry from the IoT API, persists to DB, and forwards to automation-service.
     */
    public void fetchAndStore() {
        try {
            String token = tokenRefreshService.getToken();
            if (token == null) {
                log.warn("No valid IoT token available yet — skipping telemetry fetch");
                return;
            }
            log.info("Fetching telemetry for device: {}", deviceId);
            TelemetryResponse telemetry = iotTelemetryClient.getLatestTelemetry(
                    "Bearer " + token,
                    deviceId
            );

            // Persist to H2
            SensorReading entity = SensorReading.builder()
                    .deviceId(telemetry.getDeviceId() != null ? telemetry.getDeviceId() : deviceId)
                    .temperature(telemetry.getTemperature())
                    .humidity(telemetry.getHumidity())
                    .soilMoisture(telemetry.getSoilMoisture())
                    .status(telemetry.getStatus())
                    .recordedAt(Instant.now())
                    .build();

            SensorReading saved = readingRepository.save(entity);
            log.info("Telemetry persisted (id={}): temp={}°C, humidity={}%, soil={}%",
                    saved.getId(), saved.getTemperature(), saved.getHumidity(), saved.getSoilMoisture());

            // Update fast in-memory cache
            SensorReadingResponse response = toResponse(saved);
            latestCache.set(response);

            // Forward temperature to automation-service for rule evaluation
            forwardToAutomation(saved.getTemperature());

        } catch (Exception e) {
            log.error("Failed to fetch telemetry from IoT API: {}", e.getMessage());
        }
    }

    /**
     * Returns the latest reading — from cache if available, otherwise queries DB.
     * Survives service restarts because readings are persisted.
     */
    public SensorReadingResponse getLatestReading() {
        SensorReadingResponse cached = latestCache.get();
        if (cached != null) return cached;

        // Populate cache from DB on first call after restart
        return readingRepository.findTopByOrderByRecordedAtDesc()
                .map(r -> {
                    SensorReadingResponse dto = toResponse(r);
                    latestCache.set(dto);
                    return dto;
                })
                .orElse(null);
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private void forwardToAutomation(Double temperature) {
        try {
            AutomationProcessRequest automationRequest = AutomationProcessRequest.builder()
                    .zoneId(zoneId)
                    .currentTemp(temperature)
                    .build();
            automationClient.process(resolveAuthorizationHeader(), automationRequest);
            log.info("Forwarded temp={}°C for zoneId={} to automation-service", temperature, zoneId);
        } catch (Exception e) {
            log.error("Failed to forward telemetry to automation-service: {}", e.getMessage());
        }
    }

    private String resolveAuthorizationHeader() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof String token && !token.isBlank()) {
            return "Bearer " + token;
        }

        // Scheduler-triggered flows do not have a user context; use short-lived service token.
        String serviceToken = jwtUtil.generateToken(internalServiceSubject, 300_000L);
        return "Bearer " + serviceToken;
    }

    private SensorReadingResponse toResponse(SensorReading reading) {
        return SensorReadingResponse.builder()
                .deviceId(reading.getDeviceId())
                .temperature(reading.getTemperature())
                .humidity(reading.getHumidity())
                .soilMoisture(reading.getSoilMoisture())
                .status(reading.getStatus())
                .recordedAt(reading.getRecordedAt())
                .build();
    }
}
