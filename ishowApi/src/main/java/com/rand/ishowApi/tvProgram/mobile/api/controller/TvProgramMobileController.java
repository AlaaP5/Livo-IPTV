package com.rand.ishowApi.tvProgram.mobile.api.controller;


import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramDetailsResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSectionResponse;
import com.rand.ishowApi.tvProgram.mobile.api.response.TvProgramSeasonDetailsResponse;
import com.rand.ishowApi.tvProgram.mobile.application.service.TvProgramMobileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/tvProgram/")
@RequiredArgsConstructor
public class TvProgramMobileController {

    private final TvProgramMobileService tvProgramMobileService;

    @GetMapping("sections")
    public ApiResponse<SectionBannerResponse<TvProgramSectionResponse>> getTvProgramSection() throws IOException {

        return ApiResponse.success(
                tvProgramMobileService.getTvProgramSection(),
                ApiResponseCode.SUCCESS_200,
                1L,
                1,
                1
        );
    }

    @GetMapping("sections/{sectionId}")
    public ApiResponse<List<TvProgramSectionResponse>> getTvProgramSectionById(@PathVariable Long sectionId) throws IOException {

        return ApiResponse.success(
                tvProgramMobileService.getTvProgramSectionById(sectionId),
                ApiResponseCode.SUCCESS_200

        );
    }

    @GetMapping("/{tvProgramId}")
    public ApiResponse<TvProgramDetailsResponse> getTvProgramDetails(@PathVariable String tvProgramId) {

        return ApiResponse.success(
                tvProgramMobileService.getTvProgramDetails(tvProgramId),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/season/{seasonId}")
    public ApiResponse<TvProgramSeasonDetailsResponse> getSeasonDetail(@PathVariable String seasonId) {

        return ApiResponse.success(
                tvProgramMobileService.getSeasonDetail(seasonId),
                ApiResponseCode.SUCCESS_200
        );
    }

}
