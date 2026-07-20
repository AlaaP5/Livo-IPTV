package com.rand.ishowApi.auth.mobile.controller;

import com.rand.ishowApi.account.mobile.api.request.*;
import com.rand.ishowApi.account.mobile.api.response.AccountMobileResponse;
import com.rand.ishowApi.account.mobile.application.service.AccountMobileService;
import com.rand.ishowApi.keycloak.response.TokenResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/mobile/auth")
@RequiredArgsConstructor
public class AuthMobileController {

    private final AccountMobileService service;

    @PostMapping("/login")
    public ApiResponse<AccountMobileResponse> login(
            @RequestBody AccountLoginMobileRequest request) {
        return ApiResponse.success(
                service.login(request),
                ApiResponseCode.SUCCESS_200
        );
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(
            @RequestBody AccountRegisterMobileRequest request) {
        service.register(request);
        return ApiResponse.success(
                null,
                ApiResponseCode.SUCCESS_200
        );
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(
            @RequestBody AccountRestPasswordRequest request) {
        service.restPassword(request);
        return ApiResponse.success(
                null,
                ApiResponseCode.SUCCESS_200
        );
    }

    @PostMapping("/verify")
    public ApiResponse<AccountMobileResponse> verify(
            @RequestBody AccountVerifyRequest request) {

        return ApiResponse.success(
                service.verify(request),
                ApiResponseCode.SUCCESS_200
        );
    }

    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtp(@RequestBody SendOtpRequest request) {
        service.sendOtp(request.gsm());
        return ApiResponse.success(
                null,
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/logout")
    public ApiResponse<Void> logout(
            @RequestParam String refreshToken) {
        service.logout(refreshToken);
        return ApiResponse.success(
                null,
                ApiResponseCode.SUCCESS_200
        );
    }
    @GetMapping("/refreshToken")
    public ApiResponse<TokenResponse> refreshToken(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                                   @RequestHeader("X-Refresh-Token") String refreshToken
    ) {
        TokenResponse response = service.refresh(refreshToken);
        return ApiResponse.success(response, ApiResponseCode.SUCCESS_200);
    }


}
