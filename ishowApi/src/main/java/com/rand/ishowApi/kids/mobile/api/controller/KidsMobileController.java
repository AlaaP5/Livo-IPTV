package com.rand.ishowApi.kids.mobile.api.controller;

import com.rand.ishowApi.kids.mobile.api.response.KidsMobileResponse;
import com.rand.ishowApi.kids.mobile.service.KidsMobileService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/mobile/kids")
@RequiredArgsConstructor
public class KidsMobileController {

    private final KidsMobileService kidsMobileService;

    @GetMapping
    public ApiResponse<KidsMobileResponse> getKidsMobile() throws IOException {
        return ApiResponse.success(kidsMobileService.getKidsMobile(), ApiResponseCode.SUCCESS_200);
    }
}

