package com.rand.ishowApi.auth.admin.application.service;

import com.rand.ishowApi.account.domain.entity.AccountRole;
import com.rand.ishowApi.auth.admin.api.request.LoginRequest;
import com.rand.ishowApi.keycloak.response.TokenResponse;
import com.rand.ishowApi.keycloak.service.KeyClockService;
import com.rand.ishowApi.security.jwt.JwtAuthConverter;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthAdminService {

    private final KeyClockService keycloakClient;
    private final JwtAuthConverter jwtAuthConverter;

    public TokenResponse login(LoginRequest request) {
        TokenResponse tokenResponse = keycloakClient.login(request.getUsername(), request.getPassword());

        tokenResponse.setRole(AccountRole.fromAuthorities(jwtAuthConverter.extractRolesFromToken(tokenResponse.getAccessToken())));
        return tokenResponse;
    }

    public void logout(String refreshToken) {
        keycloakClient.logout(refreshToken);
    }

    public TokenResponse refresh(String refreshToken) {
        return keycloakClient.refresh(refreshToken);
    }


}