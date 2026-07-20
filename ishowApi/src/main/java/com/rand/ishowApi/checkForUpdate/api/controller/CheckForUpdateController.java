package com.rand.ishowApi.checkForUpdate.api.controller;


import com.rand.ishowApi.checkForUpdate.api.response.CheckForUpdateResponse;
import com.rand.ishowApi.checkForUpdate.application.service.CheckForUpdateService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/mobile/check-for-update")
@RequiredArgsConstructor
public class CheckForUpdateController {

    private final CheckForUpdateService service;

    @GetMapping
    public ApiResponse<CheckForUpdateResponse> checkForUpdate() {
        return ApiResponse.success(service.checkForUpdate(), ApiResponseCode.SUCCESS_200);
    }
}
