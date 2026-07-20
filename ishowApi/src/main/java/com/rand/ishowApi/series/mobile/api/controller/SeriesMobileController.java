package com.rand.ishowApi.series.mobile.api.controller;


import com.rand.ishowApi.openSearch.response.SectionBannerResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.mobile.api.response.SeasonDetailsResponse;
import com.rand.ishowApi.series.mobile.api.response.SeriesDetailsResponse;
import com.rand.ishowApi.series.mobile.api.response.SeriesSectionResponse;
import com.rand.ishowApi.series.mobile.application.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/mobile/series/")
@RequiredArgsConstructor
class SeriesMobileController {

    private final SeriesService seriesService;

    @GetMapping("sections")
    public ApiResponse<SectionBannerResponse<SeriesSectionResponse>> getSeriesSection() throws IOException {

        return ApiResponse.success(
                seriesService.getSeriesSection(),
                ApiResponseCode.SUCCESS_200,
                1L,
                1,
                1
        );
    }

    @GetMapping("sections/{sectionId}")
    public ApiResponse<List<SeriesSectionResponse>> getSeriesSectionById(@PathVariable Long sectionId) throws IOException {

        return ApiResponse.success(
                seriesService.getSeriesSectionById(sectionId),
                ApiResponseCode.SUCCESS_200

        );
    }

    @GetMapping("/{seriesId}")
    public ApiResponse<SeriesDetailsResponse> getSeriesDetails(@PathVariable String seriesId) throws IOException {

        return ApiResponse.success(
                seriesService.getSeriesDetails(seriesId),
                ApiResponseCode.SUCCESS_200
        );
    }

    @GetMapping("/season/{seasonId}")
    public ApiResponse<SeasonDetailsResponse> getSeasonDetail(@PathVariable String seasonId) {

        return ApiResponse.success(
                seriesService.getSeasonDetail(seasonId),
                ApiResponseCode.SUCCESS_200
        );
    }

}
