package com.agms.zone_service.service;

import com.agms.zone_service.client.IoTApiClient;
import com.agms.zone_service.dto.iot.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class IoTIntegrationService {

    private final IoTApiClient iotApiClient;

    /**
     * Registers a user on the IoT platform and returns their userId.
     */
    public IoTRegisterResponse registerUser(String username, String password) {
        log.info("Registering IoT user: {}", username);
        IoTRegisterRequest request = IoTRegisterRequest.builder()
                .username(username)
                .password(password)
                .build();
        return iotApiClient.register(request);
    }

    /**
     * Logs in to the IoT platform and returns the Bearer token.
     */
    public String login(String username, String password) {
        log.info("Logging in IoT user: {}", username);
        IoTLoginRequest request = IoTLoginRequest.builder()
                .username(username)
                .password(password)
                .build();
        IoTLoginResponse response = iotApiClient.login(request);
        return response.getToken();
    }

    /**
     * Refreshes an expired access token using the provided refresh token.
     * Returns the new access token.
     */
    public IoTRefreshResponse refreshToken(String refreshToken) {
        log.info("Refreshing IoT access token");
        IoTRefreshRequest request = IoTRefreshRequest.builder()
                .refreshToken(refreshToken)
                .build();
        return iotApiClient.refresh(request);
    }

    /**
     * Registers a device on the IoT platform using the Bearer token.
     * Returns the assigned deviceId.
     */
    public String addDevice(String bearerToken, String deviceName, String deviceType, String location) {
        log.info("Adding IoT device: {} (type: {})", deviceName, deviceType);
        IoTAddDeviceRequest request = IoTAddDeviceRequest.builder()
                .deviceName(deviceName)
                .deviceType(deviceType)
                .location(location)
                .build();
        // Feign client expects full "Bearer <token>" header value
        IoTAddDeviceResponse response = iotApiClient.addDevice("Bearer " + bearerToken, request);
        return response.getDeviceId();
    }

    /**
     * Lists all devices for the authenticated user.
     */
    public List<IoTDeviceResponse> getDevices(String bearerToken) {
        log.info("Fetching IoT device list");
        return iotApiClient.getDevices("Bearer " + bearerToken);
    }
}
