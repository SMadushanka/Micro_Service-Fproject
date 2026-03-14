package com.agms.zone_service.client;

import com.agms.zone_service.dto.iot.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "iot-api-client", url = "${iot.api.base-url}")
public interface IoTApiClient {

    /**
     * Register a new user on the IoT platform.
     */
    @PostMapping("/auth/register")
    IoTRegisterResponse register(@RequestBody IoTRegisterRequest request);

    /**
     * Login to obtain a Bearer token (+ optional refresh token).
     */
    @PostMapping("/auth/login")
    IoTLoginResponse login(@RequestBody IoTLoginRequest request);

    /**
     * Refresh an expired access token using a valid refresh token.
     */
    @PostMapping("/auth/refresh")
    IoTRefreshResponse refresh(@RequestBody IoTRefreshRequest request);

    /**
     * Add a new device. Requires a valid Bearer token.
     * The Authorization header value must be prefixed with "Bearer ", e.g. "Bearer eyJ..."
     */
    @PostMapping("/devices")
    IoTAddDeviceResponse addDevice(
            @RequestHeader("Authorization") String bearerToken,
            @RequestBody IoTAddDeviceRequest request
    );

    /**
     * Retrieve all devices belonging to the authenticated user.
     */
    @GetMapping("/devices")
    List<IoTDeviceResponse> getDevices(
            @RequestHeader("Authorization") String bearerToken
    );
}
