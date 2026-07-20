package com.rand.ishowApi.home.admin.api;

import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.home.admin.application.service.HomeAdminService;
import com.rand.ishowApi.home.admin.domain.model.HomeBanner;
import com.rand.ishowApi.movie.admin.api.response.MovieAdminSectionResponse;
import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.shared.settings.ContentType;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/home/")
@RequiredArgsConstructor
public class HomeAdminController {

    private final HomeAdminService homeAdminService;

    // Movie Section Endpoints
    @GetMapping("movie/section/add")
    public ApiResponse<Void> addHomeMovieSection(
            @RequestParam String movieId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        homeAdminService.addHomeMovieSection(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("movie/section/remove")
    public ApiResponse<Void> removeHomeMovieSection(
            @RequestParam String movieId,
            @RequestParam Long sectionId) throws IOException {
        homeAdminService.removeHomeMovieSection(movieId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("movie/section/is-top")
    public ApiResponse<Void> updateHomeMovieIsTop(
            @RequestParam Long sectionId,
            @RequestParam String movieId,
            @RequestParam String isTop) throws IOException {
        homeAdminService.updateHomeMovieIsTop(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("movie/section/list")
    public ApiResponse<List<MovieAdminSectionResponse>> getHomeMovieSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<MovieAdminSectionResponse> result = homeAdminService.getHomeMovieSection(sectionId, isTop, page, size);
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
    public ApiResponse<Void> addHomeSeriesSection(
            @RequestParam String seriesId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        homeAdminService.addHomeSeriesSection(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("series/section/remove")
    public ApiResponse<Void> removeHomeSeriesSection(
            @RequestParam String seriesId,
            @RequestParam Long sectionId) throws IOException {
        homeAdminService.removeHomeSeriesSection(seriesId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("series/section/is-top")
    public ApiResponse<Void> updateHomeSeriesIsTop(
            @RequestParam Long sectionId,
            @RequestParam String seriesId,
            @RequestParam String isTop) throws IOException {
        homeAdminService.updateHomeSeriesIsTop(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("series/section/list")
    public ApiResponse<List<SeriesSectionAdminResponse>> getHomeSeriesSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<SeriesSectionAdminResponse> result = homeAdminService.getHomeSeriesSection(sectionId, null, isTop, page, size);
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
    public ApiResponse<Void> addHomeClipSection(
            @RequestParam String clipId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        homeAdminService.addHomeClipSection(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("clip/section/remove")
    public ApiResponse<Void> removeHomeClipSection(
            @RequestParam String clipId,
            @RequestParam Long sectionId) throws IOException {
        homeAdminService.removeHomeClipSection(clipId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("clip/section/is-top")
    public ApiResponse<Void> updateHomeClipIsTop(
            @RequestParam Long sectionId,
            @RequestParam String clipId,
            @RequestParam String isTop) throws IOException {
        homeAdminService.updateHomeClipIsTop(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("clip/section/list")
    public ApiResponse<List<ClipSectionAdminResponse>> getHomeClipSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<ClipSectionAdminResponse> result = homeAdminService.getHomeClipSection(sectionId,  isTop, page, size);
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
    public ApiResponse<Void> addHomeTvProgramSection(
            @RequestParam String programId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        homeAdminService.addHomeTvProgramSection(programId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("tv-program/section/remove")
    public ApiResponse<Void> removeHomeTvProgramSection(
            @RequestParam String programId,
            @RequestParam Long sectionId) throws IOException {
        homeAdminService.removeHomeTvProgramSection(programId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("tv-program/section/is-top")
    public ApiResponse<Void> updateHomeTvProgramIsTop(
            @RequestParam Long sectionId,
            @RequestParam String programId,
            @RequestParam String isTop) throws IOException {
        homeAdminService.updateHomeTvProgramIsTop(programId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("tv-program/section/list")
    public ApiResponse<List<TvProgramSectionAdminResponse>> getHomeTvProgramSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<TvProgramSectionAdminResponse> result = homeAdminService.getHomeTvProgramSection(sectionId, null, isTop, page, size);
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
    public ApiResponse<Void> addHomeChannelSection(
            @RequestParam String channelId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        homeAdminService.addHomeChannelSection(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("channel/section/remove")
    public ApiResponse<Void> removeHomeChannelSection(
            @RequestParam String channelId,
            @RequestParam Long sectionId) throws IOException {
        homeAdminService.removeHomeChannelSection(channelId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("channel/section/is-top")
    public ApiResponse<Void> updateHomeChannelIsTop(
            @RequestParam Long sectionId,
            @RequestParam String channelId,
            @RequestParam String isTop) throws IOException {
        homeAdminService.updateHomeChannelIsTop(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("channel/section/list")
    public ApiResponse<List<ChannelAdminSectionResponse>> getHomeChannelSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<ChannelAdminSectionResponse> result = homeAdminService.getHomeChannelSection(sectionId, isTop, page, size);
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }

    // Banner Endpoints
    @PostMapping("banner/movie/add")
    public ApiResponse<Void> addHomeBannerMovie(@RequestParam String movieId) throws IOException {
        homeAdminService.addHomeBannerMovie(movieId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/series/add")
    public ApiResponse<Void> addHomeBannerSeries(@RequestParam String seriesId) throws IOException {
        homeAdminService.addHomeBannerSeries(seriesId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/channel/add")
    public ApiResponse<Void> addHomeBannerChannel(@RequestParam String channelId) throws IOException {
        homeAdminService.addHomeBannerChannel(channelId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/tv-program/add")
    public ApiResponse<Void> addHomeBannerTvProgram(@RequestParam String tvProgramId) throws IOException {
        homeAdminService.addHomeBannerTvProgram(tvProgramId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/clip/add")
    public ApiResponse<Void> addHomeBannerClip(@RequestParam String clipId) throws IOException {
        homeAdminService.addHomeClip(clipId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @DeleteMapping("banner/remove")
    public ApiResponse<Void> removeHomeBanner(
            @RequestParam String contentId,
            @RequestParam ContentType contentType) throws IOException {
        homeAdminService.removeHomeBanner(contentId, contentType);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("banner/list")
    public ApiResponse<List<HomeBanner>> filterHomeBanner() throws IOException {
        List<HomeBanner> result = homeAdminService.filterHomeBanner();
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                1,
                result.size()
        );
    }
}
