package com.agms.sensor_service.client;

import com.agms.sensor_service.dto.IoTLoginRequest;
import com.agms.sensor_service.dto.IoTLoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "iot-auth-client", url = "${iot.api.base-url}")
public interface IoTAuthClient {

    /**
     * Login to obtain a fresh Bearer token from the external IoT platform.
     */
    @PostMapping("/auth/login")
    IoTLoginResponse login(@RequestBody IoTLoginRequest request);
}
