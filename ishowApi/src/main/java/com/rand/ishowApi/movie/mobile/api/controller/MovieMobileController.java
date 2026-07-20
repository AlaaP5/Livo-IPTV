package com.rand.ishowApi.movie.mobile.api.controller;


import com.rand.ishowApi.movie.mobile.api.response.MovieDetailsResponse;
import com.rand.ishowApi.movie.mobile.api.response.MovieSectionResponse;
import com.rand.ishowApi.movie.mobile.service.MovieMobileService;
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
@RequestMapping("api/v1/mobile/movie/")
@RequiredArgsConstructor
class MovieMobileController {

    private final MovieMobileService mobileService;


    @GetMapping ("sections")
    public ApiResponse<SectionBannerResponse<MovieSectionResponse>> getMoviesSection() throws IOException {

        return ApiResponse.success(
                mobileService.getMoviesSections(),
                ApiResponseCode.SUCCESS_200,
                1L,
                1,
                1
        );
    }

    @GetMapping ("sections/{sectionId}")
    public ApiResponse<List<MovieSectionResponse>> getMoviesSection(@PathVariable Long sectionId) throws IOException {

        return ApiResponse.success(
                mobileService.getMovieSectionById(sectionId),
                ApiResponseCode.SUCCESS_200

        );
    }

    @GetMapping ("/{movieId}")
    public ApiResponse<MovieDetailsResponse> getMoviesDetails(@PathVariable String movieId) throws IOException {

        return ApiResponse.success(
                mobileService.getMovieDetails(movieId),
                ApiResponseCode.SUCCESS_200
        );
    }

}
