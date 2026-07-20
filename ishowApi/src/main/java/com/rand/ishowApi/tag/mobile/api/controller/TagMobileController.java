package com.rand.ishowApi.tag.mobile.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.tag.mobile.api.response.TagMobileResponse;
import com.rand.ishowApi.tag.mobile.application.service.TagMobileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/tag/")
@RequiredArgsConstructor
public class TagMobileController {

    private final TagMobileService tagMobileService;

    @GetMapping("common")
    public ApiResponse<List<TagMobileResponse>> getCommonTags() {
        return ApiResponse.success(
                tagMobileService.getCommonTags(),
                ApiResponseCode.SUCCESS_200
        );
    }

}

