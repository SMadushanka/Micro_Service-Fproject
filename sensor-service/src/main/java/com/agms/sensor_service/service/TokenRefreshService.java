package com.agms.sensor_service.service;

import com.agms.sensor_service.client.IoTAuthClient;
import com.agms.sensor_service.dto.IoTLoginRequest;
import com.agms.sensor_service.dto.IoTLoginResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Maintains a fresh IoT Bearer token by automatically logging in at startup
 * and re-authenticating every 12 minutes (token lifetime is typically 15 minutes).
 *
 * If login fails at startup the stale token from config is used as a fallback.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenRefreshService {

    private final IoTAuthClient iotAuthClient;

    @Value("${iot.api.username}")
    private String username;

    @Value("${iot.api.password}")
    private String password;

    @Value("${iot.api.bearer-token:}")
    private String fallbackToken;

    private final AtomicReference<String> currentToken = new AtomicReference<>();

    /**
     * Perform initial login as soon as the bean is ready.
     */
    @PostConstruct
    public void init() {
        refresh();
    }

    /**
     * Refresh the token every 12 minutes (720 000 ms).
     * Running more frequently than needed ensures we never hit an edge-case expiry.
     */
    @Scheduled(fixedDelay = 720_000)
    public void refresh() {
        try {
            log.info("Refreshing IoT bearer token for user: {}", username);
            IoTLoginResponse response = iotAuthClient.login(
                    IoTLoginRequest.builder()
                            .username(username)
                            .password(password)
                            .build());
            if (response.getToken() != null) {
                currentToken.set(response.getToken());
                log.info("IoT bearer token refreshed successfully");
            } else {
                log.warn("IoT login returned null token — keeping previous token");
            }
        } catch (Exception e) {
            log.error("IoT token refresh failed: {}. Using previous token.", e.getMessage());
            // On first startup, fall back to the static token from config
            if (currentToken.get() == null && !fallbackToken.isBlank()) {
                currentToken.set(fallbackToken);
                log.warn("Falling back to static bearer-token from config");
            }
        }
    }

    /**
     * Returns the current valid Bearer token (without the "Bearer " prefix).
     */
    public String getToken() {
        return currentToken.get();
    }
}
