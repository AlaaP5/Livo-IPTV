package com.rand.ishowApi.sport.mobile.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.sport.mobile.api.response.SportMobileResponse;
import com.rand.ishowApi.sport.mobile.service.SportMobileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/mobile/sport")
@RequiredArgsConstructor
public class SportMobileController {

    private final SportMobileService sportMobileService;

    @GetMapping
    public ApiResponse<SportMobileResponse> getSportMobile() throws IOException {
        return ApiResponse.success(sportMobileService.getSportMobile(), ApiResponseCode.SUCCESS_200);
    }
}
