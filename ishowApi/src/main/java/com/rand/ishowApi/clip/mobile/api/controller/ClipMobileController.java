package com.rand.ishowApi.clip.mobile.api.controller;


import com.rand.ishowApi.clip.mobile.api.response.ClipSectionResponse;
import com.rand.ishowApi.clip.mobile.api.response.ClipsDetailsResponse;
import com.rand.ishowApi.clip.mobile.application.ClipMobileService;
import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/clip/")
@RequiredArgsConstructor
class ClipMobileController {

    private final ClipMobileService mobileService;


    @GetMapping("sections")
    public ApiResponse<SectionBannerResponse<ClipSectionResponse>> getClipsSection() throws IOException {

        return ApiResponse.success(
                mobileService.getClipsSections(),
                ApiResponseCode.SUCCESS_200,
                1L,
                1,
                1
        );
    }

    @GetMapping("sections/{sectionId}")
    public ApiResponse<List<ClipSectionResponse>> getClipsSection(@PathVariable Long sectionId) throws IOException {

        return ApiResponse.success(
                mobileService.getClipsSectionById(sectionId),
                ApiResponseCode.SUCCESS_200

        );
    }

    @GetMapping("{clipId}")
    public ApiResponse<ClipsDetailsResponse> getClipDetails(@PathVariable String clipId) throws IOException {

        return ApiResponse.success(
                mobileService.getClipDetails(clipId),
                ApiResponseCode.SUCCESS_200
        );
    }

}
