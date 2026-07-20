package com.rand.ishowApi.account.mobile.api.controller;


import com.rand.ishowApi.account.mobile.api.request.AccountChangePasswordRequest;
import com.rand.ishowApi.account.mobile.api.request.AccountEditProfileRequest;
import com.rand.ishowApi.account.mobile.api.response.ProfileMobileResponse;
import com.rand.ishowApi.account.mobile.application.service.AccountMobileService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mobile/account")
@RequiredArgsConstructor
public class AccountMobileController {

    private final AccountMobileService service;

    @DeleteMapping
    public ApiResponse<Void> delete() {
        service.delete();
        return ApiResponse.success(
                null,
                ApiResponseCode.SUCCESS_200
        );
    }


    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(
            @RequestBody AccountChangePasswordRequest request) {
        service.changePassword(request);
        return ApiResponse.success(
                null,
                ApiResponseCode.SUCCESS_200
        );
    }


    @PostMapping("/edit-profile")
    public ApiResponse<ProfileMobileResponse> editProfile(@RequestBody AccountEditProfileRequest request) {
        return ApiResponse.success(service.editProfile(request), ApiResponseCode.SUCCESS_200);
    }
}
