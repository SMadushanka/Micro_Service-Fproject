package com.agms.sensor_service.scheduler;

import com.agms.sensor_service.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelemetryScheduler {

    private final SensorService sensorService;

    /**
     * Runs every 10 seconds (fixed delay starts after previous execution completes).
     * Use fixedRate = 10000 if you want exactly every 10s regardless of execution time.
     */
    @Scheduled(fixedDelay = 10000)
    public void pollTelemetry() {
        log.debug("Scheduler triggered: polling IoT telemetry");
        sensorService.fetchAndStore();
    }
}
