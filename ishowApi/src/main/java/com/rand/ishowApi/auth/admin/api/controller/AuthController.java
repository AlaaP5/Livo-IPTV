package com.rand.ishowApi.auth.admin.api.controller;

import com.rand.ishowApi.auth.admin.api.request.LoginRequest;
import com.rand.ishowApi.auth.admin.application.service.AuthAdminService;
import com.rand.ishowApi.keycloak.response.TokenResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/admin/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthAdminService authService;

    @PostMapping("/login")
    public ApiResponse<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ApiResponse.success(response, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/refreshToken")
    public ApiResponse<TokenResponse> refreshToken(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                   @RequestHeader("X-Refresh-Token") String refreshToken
    ) {
        TokenResponse response = authService.refresh(refreshToken);
        return ApiResponse.success(response, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/logout")
    public ApiResponse<Void> logout(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                    @RequestHeader("X-Refresh-Token") String refreshToken) {
        authService.logout(refreshToken);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

}