package com.agms.automation_service.service;

import com.agms.automation_service.client.ZoneClient;
import com.agms.automation_service.dto.AutomationLogResponse;
import com.agms.automation_service.dto.ProcessRequest;
import com.agms.automation_service.dto.ZoneResponse;
import com.agms.automation_service.model.AutomationAction;
import com.agms.automation_service.model.AutomationLog;
import com.agms.automation_service.repository.AutomationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutomationService {

    private final AutomationLogRepository logRepository;
    private final ZoneClient zoneClient;

    /**
     * Evaluates temperature against zone thresholds and logs the resulting action.
     *
     * Rules:
     *   currentTemp > maxTemp → TURN_FAN_ON
     *   currentTemp < minTemp → TURN_HEATER_ON
     *   otherwise             → NO_ACTION
     */
    @Transactional
    public AutomationLogResponse process(ProcessRequest request) {
        log.info("Processing automation for zoneId={}, temp={}°C",
                request.getZoneId(), request.getCurrentTemp());

        // Fetch zone thresholds via Feign → zone-service
        ZoneResponse zone = zoneClient.getZoneById(request.getZoneId());
        log.info("Zone thresholds: minTemp={}, maxTemp={}", zone.getMinTemp(), zone.getMaxTemp());

        if (zone.getMinTemp() == null || zone.getMaxTemp() == null) {
            throw new IllegalStateException(
                    "Zone " + request.getZoneId() + " has no temperature thresholds configured");
        }

        // Apply automation rules
        AutomationAction action = evaluateAction(
                request.getCurrentTemp(), zone.getMinTemp(), zone.getMaxTemp());
        log.info("Action determined: {}", action);

        // Persist log
        AutomationLog log = AutomationLog.builder()
                .zoneId(request.getZoneId())
                .temperature(request.getCurrentTemp())
                .action(action)
                .timestamp(Instant.now())
                .build();

        AutomationLog saved = logRepository.save(log);
        return toResponse(saved);
    }

    public List<AutomationLogResponse> getAllLogs() {
        return logRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AutomationLogResponse> getLogsByZone(Long zoneId) {
        return logRepository.findByZoneIdOrderByTimestampDesc(zoneId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Private helpers ──────────────────────────────────────────────────────

    private AutomationAction evaluateAction(Double current, Double min, Double max) {
        if (current > max) return AutomationAction.TURN_FAN_ON;
        if (current < min) return AutomationAction.TURN_HEATER_ON;
        return AutomationAction.NO_ACTION;
    }

    private AutomationLogResponse toResponse(AutomationLog log) {
        return AutomationLogResponse.builder()
                .id(log.getId())
                .zoneId(log.getZoneId())
                .temperature(log.getTemperature())
                .action(log.getAction())
                .timestamp(log.getTimestamp())
                .build();
    }
}
