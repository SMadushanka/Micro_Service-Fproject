package com.agms.automation_service.controller;

import com.agms.automation_service.dto.AutomationLogResponse;
import com.agms.automation_service.dto.ProcessRequest;
import com.agms.automation_service.service.AutomationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/automation")
@RequiredArgsConstructor
public class AutomationController {

    private final AutomationService automationService;

    /**
     * Evaluate temperature for a zone and trigger the appropriate action.
     *
     * POST /api/automation/process
     * Body: { "zoneId": 1, "currentTemp": 35.5 }
     */
    @PostMapping("/process")
    public ResponseEntity<AutomationLogResponse> process(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @Valid @RequestBody ProcessRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(automationService.process(authorization, request));
    }

    /**
     * Retrieve all automation logs.
     * GET /api/automation/logs
     *
     * Optionally filter by zone:
     * GET /api/automation/logs?zoneId=1
     */
    @GetMapping("/logs")
    public ResponseEntity<List<AutomationLogResponse>> getLogs(
            @RequestParam(required = false) Long zoneId) {
        if (zoneId != null) {
            return ResponseEntity.ok(automationService.getLogsByZone(zoneId));
        }
        return ResponseEntity.ok(automationService.getAllLogs());
    }
}
