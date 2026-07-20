package com.rand.ishowApi.kids.admin.api;

import com.rand.ishowApi.channel.admin.api.response.ChannelAdminSectionResponse;
import com.rand.ishowApi.clip.admin.api.response.ClipSectionAdminResponse;
import com.rand.ishowApi.kids.admin.application.service.KidsAdminService;
import com.rand.ishowApi.kids.admin.domain.model.KidsBanner;
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
@RequestMapping("api/v1/admin/kids/")
@RequiredArgsConstructor
public class KidsAdminController {

    private final KidsAdminService kidsAdminService;

    // Movie Section Endpoints
    @GetMapping("movie/section/add")
    public ApiResponse<Void> addKidsMovieSection(
            @RequestParam String movieId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        kidsAdminService.addKidsMovieSection(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("movie/section/remove")
    public ApiResponse<Void> removeKidsMovieSection(
            @RequestParam String movieId,
            @RequestParam Long sectionId) throws IOException {
        kidsAdminService.removeKidsMovieSection(movieId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PutMapping("movie/section/is-top")
    public ApiResponse<Void> updateKidsMovieIsTop(
            @RequestParam Long sectionId,
            @RequestParam String movieId,
            @RequestParam String isTop) throws IOException {
        kidsAdminService.updateKidsMovieIsTop(movieId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("movie/section/list")
    public ApiResponse<List<MovieAdminSectionResponse>> getKidsMovieSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<MovieAdminSectionResponse> result = kidsAdminService.getKidsMovieSection(sectionId, isTop, page, size);
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
    public ApiResponse<Void> addKidsSeriesSection(
            @RequestParam String seriesId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        kidsAdminService.addKidsSeriesSection(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("series/section/remove")
    public ApiResponse<Void> removeKidsSeriesSection(
            @RequestParam String seriesId,
            @RequestParam Long sectionId) throws IOException {
        kidsAdminService.removeKidsSeriesSection(seriesId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PutMapping("series/section/is-top")
    public ApiResponse<Void> updateKidsSeriesIsTop(
            @RequestParam Long sectionId,
            @RequestParam String seriesId,
            @RequestParam String isTop) throws IOException {
        kidsAdminService.updateKidsSeriesIsTop(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("series/section/list")
    public ApiResponse<List<SeriesSectionAdminResponse>> getKidsSeriesSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<SeriesSectionAdminResponse> result = kidsAdminService.getKidsSeriesSection(sectionId, isTop, page, size);
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
    public ApiResponse<Void> addKidsClipSection(
            @RequestParam String clipId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        kidsAdminService.addKidsClipSection(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("clip/section/remove")
    public ApiResponse<Void> removeKidsClipSection(
            @RequestParam String clipId,
            @RequestParam Long sectionId) throws IOException {
        kidsAdminService.removeKidsClipSection(clipId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PutMapping("clip/section/is-top")
    public ApiResponse<Void> updateKidsClipIsTop(
            @RequestParam Long sectionId,
            @RequestParam String clipId,
            @RequestParam String isTop) throws IOException {
        kidsAdminService.updateKidsClipIsTop(clipId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("clip/section/list")
    public ApiResponse<List<ClipSectionAdminResponse>> getKidsClipSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<ClipSectionAdminResponse> result = kidsAdminService.getKidsClipSection(sectionId, isTop, page, size);
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
    public ApiResponse<Void> addKidsTvProgramSection(
            @RequestParam String programId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        kidsAdminService.addKidsTvProgramSection(programId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("tv-program/section/remove")
    public ApiResponse<Void> removeKidsTvProgramSection(
            @RequestParam String programId,
            @RequestParam Long sectionId) throws IOException {
        kidsAdminService.removeKidsTvProgramSection(programId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PutMapping("tv-program/section/is-top")
    public ApiResponse<Void> updateKidsTvProgramIsTop(
            @RequestParam Long sectionId,
            @RequestParam String programId,
            @RequestParam String isTop) throws IOException {
        kidsAdminService.updateKidsTvProgramIsTop(programId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("tv-program/section/list")
    public ApiResponse<List<TvProgramSectionAdminResponse>> getKidsTvProgramSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<TvProgramSectionAdminResponse> result = kidsAdminService.getKidsTvProgramSection(sectionId, isTop, page, size);
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
    public ApiResponse<Void> addKidsChannelSection(
            @RequestParam String channelId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop) throws IOException {
        kidsAdminService.addKidsChannelSection(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("channel/section/remove")
    public ApiResponse<Void> removeKidsChannelSection(
            @RequestParam String channelId,
            @RequestParam Long sectionId) throws IOException {
        kidsAdminService.removeKidsChannelSection(channelId, sectionId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PutMapping("channel/section/is-top")
    public ApiResponse<Void> updateKidsChannelIsTop(
            @RequestParam Long sectionId,
            @RequestParam String channelId,
            @RequestParam String isTop) throws IOException {
        kidsAdminService.updateKidsChannelIsTop(channelId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("channel/section/list")
    public ApiResponse<List<ChannelAdminSectionResponse>> getKidsChannelSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        List<ChannelAdminSectionResponse> result = kidsAdminService.getKidsChannelSection(sectionId, isTop, page, size);
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
    public ApiResponse<Void> addKidsBannerMovie(@RequestParam String movieId) throws IOException {
        kidsAdminService.addKidsBannerMovie(movieId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/series/add")
    public ApiResponse<Void> addKidsBannerSeries(@RequestParam String seriesId) throws IOException {
        kidsAdminService.addKidsBannerSeries(seriesId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/channel/add")
    public ApiResponse<Void> addKidsBannerChannel(@RequestParam String channelId) throws IOException {
        kidsAdminService.addKidsBannerChannel(channelId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/tv-program/add")
    public ApiResponse<Void> addKidsBannerTvProgram(@RequestParam String tvProgramId) throws IOException {
        kidsAdminService.addKidsBannerTvProgram(tvProgramId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("banner/clip/add")
    public ApiResponse<Void> addKidsClip(@RequestParam String clipId) throws IOException {
        kidsAdminService.addKidsClip(clipId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @DeleteMapping("banner/remove")
    public ApiResponse<Void> removeKidsBanner(
            @RequestParam String contentId,
            @RequestParam ContentType contentType) throws IOException {
        kidsAdminService.removeKidsBanner(contentId, contentType);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("banner/list")
    public ApiResponse<List<KidsBanner>> filterKidsBanner() throws IOException {
        List<KidsBanner> result = kidsAdminService.filterKidsBanner();
        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                1,
                result.size()
        );
    }
}

