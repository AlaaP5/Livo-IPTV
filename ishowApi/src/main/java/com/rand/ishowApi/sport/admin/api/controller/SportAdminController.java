package com.rand.ishowApi.sport.admin.api.controller;


import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.sport.admin.application.service.SportAdminService;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/sport/")
@RequiredArgsConstructor
public class SportAdminController {

    private final SportAdminService sportAdminService;


    // Movie Section Endpoints
    @GetMapping("movie/section/add")
    public ApiResponse<Void> addSportMovieSection(
            @RequestParam String movieId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        sportAdminService.addSportMovieSection(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("movie/section/remove")
    public ApiResponse<Void> removeSportMovieSection(
            @RequestParam String movieId,
            @RequestParam Long sectionId) throws IOException {
        sportAdminService.removeSportMovieSection(movieId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("movie/section/is-top")
    public ApiResponse<Void> updateSportMovieIsTop(
            @RequestParam Long sectionId,
            @RequestParam String movieId,
            @RequestParam String isTop) throws IOException {
        sportAdminService.updateSportMovieIsTop(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("movie/section/list")
    public ApiResponse<List<MovieAdminSectionResponse>> getSportMovieSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<MovieAdminSectionResponse> result = sportAdminService.getSportMovieSection(sectionId, isTop, page, size);
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }


    // Series Section Endpoints
    @GetMapping("series/section/add")
    public ApiResponse<Void> addSportSeriesSection(
            @RequestParam String seriesId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        sportAdminService.addSportSeriesSection(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("series/section/remove")
    public ApiResponse<Void> removeSportSeriesSection(
            @RequestParam String seriesId,
            @RequestParam Long sectionId) throws IOException {
        sportAdminService.removeSportSeriesSection(seriesId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("series/section/is-top")
    public ApiResponse<Void> updateSportSeriesIsTop(
            @RequestParam Long sectionId,
            @RequestParam String seriesId,
            @RequestParam String isTop) throws IOException {
        sportAdminService.updateSportSeriesIsTop(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("series/section/list")
    public ApiResponse<List<SeriesSectionAdminResponse>> getSportSeriesSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<SeriesSectionAdminResponse> result = sportAdminService.getSportSeriesSection(sectionId, isTop, page, size);
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }


    // Clip Section Endpoints
    @GetMapping("clip/section/add")
    public ApiResponse<Void> addSportClipSection(
            @RequestParam String clipId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        sportAdminService.addSportClipSection(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("clip/section/remove")
    public ApiResponse<Void> removeSportClipSection(
            @RequestParam String clipId,
            @RequestParam Long sectionId) throws IOException {
        sportAdminService.removeSportClipSection(clipId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("clip/section/is-top")
    public ApiResponse<Void> updateSportClipIsTop(
            @RequestParam Long sectionId,
            @RequestParam String clipId,
            @RequestParam String isTop) throws IOException {
        sportAdminService.updateSportClipIsTop(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("clip/section/list")
    public ApiResponse<List<ClipSectionAdminResponse>> getSportClipSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<ClipSectionAdminResponse> result = sportAdminService.getSportClipSection(sectionId, isTop, page, size);
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }


    // TV Program Section Endpoints
    @GetMapping("tv-program/section/add")
    public ApiResponse<Void> addSportTvProgramSection(
            @RequestParam String programId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        sportAdminService.addSportTvProgramSection(programId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("tv-program/section/remove")
    public ApiResponse<Void> removeSportTvProgramSection(
            @RequestParam String programId,
            @RequestParam Long sectionId) throws IOException {
        sportAdminService.removeSportTvProgramSection(programId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("tv-program/section/is-top")
    public ApiResponse<Void> updateSportTvProgramIsTop(
            @RequestParam Long sectionId,
            @RequestParam String programId,
            @RequestParam String isTop) throws IOException {
        sportAdminService.updateSportTvProgramIsTop(programId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("tv-program/section/list")
    public ApiResponse<List<TvProgramSectionAdminResponse>> getSportTvProgramSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<TvProgramSectionAdminResponse> result = sportAdminService.getSportTvProgramSection(sectionId, isTop, page, size);
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }


    // Channel Section Endpoints
    @GetMapping("channel/section/add")
    public ApiResponse<Void> addSportChannelSection(
            @RequestParam String channelId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        sportAdminService.addSportChannelSection(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("channel/section/remove")
    public ApiResponse<Void> removeSportChannelSection(
            @RequestParam String channelId,
            @RequestParam Long sectionId) throws IOException {
        sportAdminService.removeSportChannelSection(channelId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("channel/section/is-top")
    public ApiResponse<Void> updateSportChannelIsTop(
            @RequestParam Long sectionId,
            @RequestParam String channelId,
            @RequestParam String isTop) throws IOException {
        sportAdminService.updateSportChannelIsTop(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("channel/section/list")
    public ApiResponse<List<ChannelAdminSectionResponse>> getSportChannelSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<ChannelAdminSectionResponse> result = sportAdminService.getSportChannelSection(sectionId, isTop, page, size);
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }
}
