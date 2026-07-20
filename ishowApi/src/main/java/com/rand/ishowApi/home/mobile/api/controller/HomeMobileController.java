package com.rand.ishowApi.home.mobile.api.controller;

import com.rand.ishowApi.home.mobile.api.response.HomeMobileResponse;
import com.rand.ishowApi.home.mobile.service.HomeMobileService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/mobile/home")
@RequiredArgsConstructor
public class HomeMobileController {

    private final HomeMobileService mobileService;

    @GetMapping
    public ApiResponse<HomeMobileResponse> getHomeMobile() throws IOException {
        return ApiResponse.success(mobileService.getHomeMobile(), ApiResponseCode.SUCCESS_200);
    }


}

