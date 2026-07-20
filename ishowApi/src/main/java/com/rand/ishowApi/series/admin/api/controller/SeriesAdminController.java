package com.rand.ishowApi.series.admin.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.series.admin.api.request.*;
import com.rand.ishowApi.series.admin.api.response.SeriesSectionAdminResponse;
import com.rand.ishowApi.series.admin.application.service.SeriesAdminService;
import com.rand.ishowApi.series.admin.application.service.SeriesEpisodeAdminService;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.series.admin.application.service.SeriesSeasonAdminService;
import com.rand.ishowApi.series.admin.domain.entity.Series;
import com.rand.ishowApi.series.admin.domain.entity.SeriesEpisode;
import com.rand.ishowApi.series.admin.domain.entity.SeriesSeason;
import com.rand.ishowApi.series.admin.api.response.SeriesAdminResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/series")
@RequiredArgsConstructor
public class SeriesAdminController {

    private final SeriesAdminService seriesAdminService;
    private final SeriesSeasonAdminService seasonAdminService;
    private final SeriesEpisodeAdminService episodeAdminService;

    // ================================================
    // =========== Series
    // =================================================

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Series> createSeries(@ModelAttribute AddSeriesAdminRequest request) {
        return ApiResponse.success(seriesAdminService.addSeries(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<Series> updateSeries(@ModelAttribute UpdateSeriesAdminRequest request) throws IOException {
        return ApiResponse.success(seriesAdminService.updateSeries(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{seriesId}")
    public ApiResponse<Series> findSeriesById(@PathVariable String seriesId) {
        return ApiResponse.success(seriesAdminService.findSeriesById(seriesId), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/filter")
    public ApiResponse<List<SeriesAdminResponse>> filterSeries(@RequestBody FilterSeriesAdminRequest request) {
        Page<SeriesAdminResponse> page = seriesAdminService.filter(request);
        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @GetMapping("/publish/{seriesId}")
    public ApiResponse<Series> publishSeries(@PathVariable String seriesId) throws IOException {
        return ApiResponse.success(seriesAdminService.publishSeries(seriesId), ApiResponseCode.SUCCESS_200);
    }

    // ================================================
    // =========== Season
    // =================================================

    @PostMapping(value = "/season/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SeriesSeason> createSeriesSeason(@ModelAttribute AddSeriesSeasonAdminRequest request) {
        return ApiResponse.success(seasonAdminService.addSeriesSeason(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/season/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SeriesSeason> updateSeriesSeason(@ModelAttribute UpdateSeriesSeasonAdminRequest request) {
        return ApiResponse.success(seasonAdminService.updateSeriesSeason(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{seriesId}/season")
    public ApiResponse<List<SeriesSeason>> findSeriesSeasonById(@PathVariable String seriesId) {
        return ApiResponse.success(seasonAdminService.findSeriesSeasonBySeriesId(seriesId), ApiResponseCode.SUCCESS_200);
    }


    // ================================================
    // =========== Episode
    // =================================================

    @PostMapping("/episode/create")
    public ApiResponse<SeriesEpisode> createSeriesEpisode(@ModelAttribute AddSeriesEpisodeAdminRequest request) {
        return ApiResponse.success(episodeAdminService.addSeriesEpisode(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/episode/update")
    public ApiResponse<SeriesEpisode> updateSeriesEpisode(@ModelAttribute UpdateSeriesEpisodeAdminRequest request) {
        return ApiResponse.success(episodeAdminService.updateSeriesEpisode(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{seriesId}/season/{seasonId}/episode")
    public ApiResponse<List<SeriesEpisode>> findSeriesEpisodeById(
            @PathVariable String seriesId,
            @PathVariable String seasonId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<SeriesEpisode> episodePage = episodeAdminService.findSeriesEpisodeBySeasonId(seriesId, seasonId, page, size);
        return ApiResponse.success(
                episodePage.getContent(),
                ApiResponseCode.SUCCESS_200,
                episodePage.getTotalElements(),
                episodePage.getNumber() + 1,
                episodePage.getSize()
        );
    }

    @GetMapping("/episode/publish/{episodeId}")
    public ApiResponse<SeriesEpisode> publishEpisode(@PathVariable String episodeId) throws IOException {
        return ApiResponse.success(episodeAdminService.publishEpisode(episodeId), ApiResponseCode.SUCCESS_200);
    }

    //TODO remove
    @GetMapping("/episode/updateTranscode/{episodeId}")
    public ApiResponse<Void> updateEpisodeTranscodeResult(@PathVariable String episodeId) throws IOException {

        TranscodeMetaData metaData = new TranscodeMetaData();
        metaData.setMasterPlaylist("");
        metaData.setVariants(new ArrayList<>());

        episodeAdminService.updateSeriesEpisodeTranscodeResult(episodeId, metaData);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    // ================================================
    // =========== Section
    // =================================================


    @GetMapping("/section/add")
    public ApiResponse<Void> addSeriesToSection(@RequestParam String seriesId,
                                                @RequestParam Long sectionId,
                                                @RequestParam(required = false) String isTop) throws IOException {

        seriesAdminService.addSeriesToSection(seriesId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/section/remove")
    public ApiResponse<Void> removeSeriesFromSection(@RequestParam Long sectionId,
                                                     @RequestParam String seriesId) throws IOException {

        seriesAdminService.removeSeriesFromSection(sectionId, seriesId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/section/list")
    public ApiResponse<List<SeriesSectionAdminResponse>> getSeriesSection(@RequestParam Long sectionId,
                                                                          @RequestParam(required = false) String isTop,
                                                                          @RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int size) throws IOException {

        List<SeriesSectionAdminResponse> result = seriesAdminService.getSeriesSection(sectionId, isTop, page, size);

        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }

    @PutMapping("/section/is-top")
    public ApiResponse<Void> updateSeriesIsTop(@RequestParam Long sectionId,
                                               @RequestParam String seriesId,
                                               @RequestParam String isTop) throws IOException {
        seriesAdminService.updateSeriesIsTop(sectionId, seriesId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }


}

