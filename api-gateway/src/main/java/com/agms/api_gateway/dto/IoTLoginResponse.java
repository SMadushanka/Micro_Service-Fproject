package com.agms.api_gateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Subset of the response returned by the external IoT platform's POST /auth/login.
 * Used only to verify that credentials are valid — the token itself is discarded.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IoTLoginResponse {
    private String token;
    private String tokenType;
    private Long expiresIn;
}
