package com.rand.ishowApi.channel.mobile.api.controller;

import com.rand.ishowApi.channel.mobile.api.response.ChannelDetailsResponse;
import com.rand.ishowApi.channel.mobile.api.response.ChannelSectionResponse;
import com.rand.ishowApi.channel.mobile.application.ChannelMobileService;
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
@RequestMapping("api/v1/mobile/channel/")
@RequiredArgsConstructor
public class ChannelMobileController {

    private final ChannelMobileService channelMobileService;

    @GetMapping("sections")
    public ApiResponse<SectionBannerResponse<ChannelSectionResponse>> getChannelSections() throws IOException {
        return ApiResponse.success(
                channelMobileService.getChannelSections(),
                ApiResponseCode.SUCCESS_200,
                1L,
                1,
                1
        );
    }

    @GetMapping("sections/{sectionId}")
    public ApiResponse<List<ChannelSectionResponse>> getChannelSectionById(@PathVariable Long sectionId) throws IOException {
        return ApiResponse.success(
                channelMobileService.getChannelSectionById(sectionId),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/{channelId}")
    public ApiResponse<ChannelDetailsResponse> getChannelDetails(@PathVariable String channelId) {
        return ApiResponse.success(
                channelMobileService.getChannelDetails(channelId),
                ApiResponseCode.SUCCESS_200
        );
    }
}

