package com.agms.api_gateway.controller;

import com.agms.api_gateway.dto.IoTLoginResponse;
import com.agms.api_gateway.dto.LoginRequest;
import com.agms.api_gateway.dto.LoginResponse;
import com.agms.api_gateway.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Public authentication endpoint on the AGMS API Gateway.
 *
 * Flow:
 *   1. Client POSTs username + password to POST /auth/login
 *   2. Gateway forwards credentials to the external IoT platform for validation
 *   3. If IoT login succeeds → gateway issues its own AGMS JWT (24h)
 *   4. Client uses the AGMS JWT as: Authorization: Bearer <token> for all subsequent calls
 *
 * The AGMS JWT is signed with the gateway's own secret — it is NOT the IoT platform token.
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Value("${iot.api.base-url}")
    private String iotBaseUrl;

    /**
     * POST /auth/login
     * Body: { "username": "kavindu", "password": "yourpassword" }
     * Returns: { "token": "<agms-jwt>", "tokenType": "Bearer", "subject": "kavindu" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        // Validate credentials against the IoT platform
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<IoTLoginResponse> iotResponse = restTemplate.exchange(
                    iotBaseUrl + "/auth/login",
                    HttpMethod.POST,
                    entity,
                    IoTLoginResponse.class
            );

            if (iotResponse.getStatusCode().is2xxSuccessful()
                    && iotResponse.getBody() != null
                    && iotResponse.getBody().getToken() != null) {

                // IoT credentials valid — issue an AGMS JWT
                String agmsToken = jwtUtil.generateToken(request.getUsername());
                log.info("AGMS JWT issued for user: {}", request.getUsername());
                return ResponseEntity.ok(new LoginResponse(agmsToken, "Bearer", request.getUsername()));
            }

            log.warn("IoT login returned unexpected response for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid credentials"));

        } catch (HttpClientErrorException.Unauthorized e) {
            log.warn("IoT login rejected for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized", "message", "Invalid username or password"));
        } catch (Exception e) {
            log.error("Login failed due to IoT API error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(Map.of("error", "Bad Gateway", "message", "Could not reach IoT authentication service"));
        }
    }
}
