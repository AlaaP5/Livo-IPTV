package com.rand.ishowApi.helpCenter.mobile.api.controller;

import com.rand.ishowApi.helpCenter.mobile.api.response.HelpCenterMobileResponse;
import com.rand.ishowApi.helpCenter.mobile.application.service.HelpCenterMobileService;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/mobile/helpCenter")
@RequiredArgsConstructor
public class HelpCenterMobileController {

    private final HelpCenterMobileService helpCenterMobileService;

    @GetMapping
    public ApiResponse<HelpCenterMobileResponse> getHelpCenter() throws IOException {
        HelpCenterMobileResponse response =
                helpCenterMobileService.getHelpCenter();

        long count = response.getHelpCenterMetas() == null
                ? 0
                : response.getHelpCenterMetas().size();

        return ApiResponse.success(
                response,
                ApiResponseCode.SUCCESS_200,
                count,
                1,
                (int) count
        );
    }
}
