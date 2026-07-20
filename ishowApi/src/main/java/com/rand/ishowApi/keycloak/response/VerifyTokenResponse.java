package com.rand.ishowApi.keycloak.response;

import lombok.Data;

@Data
public class VerifyTokenResponse {
    private boolean valid;
    private String username;
    private Long tenantId;
}

