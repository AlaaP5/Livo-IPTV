package com.rand.ishowApi.tvProgram.admin.api.controller;

import com.rand.ishowApi.response.ApiResponse;
import com.rand.ishowApi.response.ApiResponseCode;
import com.rand.ishowApi.metadata.video.TranscodeMetaData;
import com.rand.ishowApi.tvProgram.admin.api.request.*;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramAdminResponse;
import com.rand.ishowApi.tvProgram.admin.api.response.TvProgramSectionAdminResponse;
import com.rand.ishowApi.tvProgram.admin.application.service.TvProgramAdminService;
import com.rand.ishowApi.tvProgram.admin.application.service.TvProgramEpisodeAdminService;
import com.rand.ishowApi.tvProgram.admin.application.service.TvProgramSeasonAdminService;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgram;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramEpisode;
import com.rand.ishowApi.tvProgram.admin.domain.entity.TvProgramSeason;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/tv-program")
@RequiredArgsConstructor
public class TvProgramAdminController {

    private final TvProgramAdminService tvProgramAdminService;
    private final TvProgramSeasonAdminService tvProgramSeasonAdminService;
    private final TvProgramEpisodeAdminService tvProgramEpisodeAdminService;

    // ================================================
    // =========== TvProgram
    // =================================================

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TvProgram> createTvProgram(@ModelAttribute AddTvProgramAdminRequest request) {
        return ApiResponse.success(tvProgramAdminService.addTvProgram(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TvProgram> updateTvProgram(@ModelAttribute UpdateTvProgramAdminRequest request) throws IOException {
        return ApiResponse.success(tvProgramAdminService.updateTvProgram(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{tvProgramId}")
    public ApiResponse<TvProgram> findTvProgramById(@PathVariable String tvProgramId) {
        return ApiResponse.success(tvProgramAdminService.findTvProgramById(tvProgramId), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/filter")
    public ApiResponse<List<TvProgramAdminResponse>> filterTvProgram(@RequestBody FilterTvProgramAdminRequest request) {
        Page<TvProgramAdminResponse> page = tvProgramAdminService.filter(request);
        return ApiResponse.success(
                page.getContent(),
                ApiResponseCode.SUCCESS_200,
                page.getTotalElements(),
                page.getNumber() + 1,
                page.getSize()
        );
    }

    @GetMapping("/publish/{tvProgramId}")
    public ApiResponse<TvProgram> publishTvProgram(@PathVariable String tvProgramId) {
        return ApiResponse.success(tvProgramAdminService.publishTvProgram(tvProgramId), ApiResponseCode.SUCCESS_200);
    }

    // ================================================
    // =========== Season
    // =================================================

    @PostMapping(value = "/season/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TvProgramSeason> createTvProgramSeason(@ModelAttribute AddTvProgramSeasonAdminRequest request) {
        return ApiResponse.success(tvProgramSeasonAdminService.addTvProgramSeason(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping(value = "/season/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TvProgramSeason> updateTvProgramSeason(@ModelAttribute UpdateTvProgramSeasonAdminRequest request) {
        return ApiResponse.success(tvProgramSeasonAdminService.updateTvProgramSeason(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{tvProgramId}/season")
    public ApiResponse<List<TvProgramSeason>> findTvProgramSeasonById(@PathVariable String tvProgramId) {
        return ApiResponse.success(tvProgramSeasonAdminService.findTvProgramSeasonByTvProgramId(tvProgramId), ApiResponseCode.SUCCESS_200);
    }

    // ================================================
    // =========== Episode
    // =================================================

    @PostMapping("/episode/create")
    public ApiResponse<TvProgramEpisode> createTvProgramEpisode(@ModelAttribute AddTvProgramEpisodeAdminRequest request) {
        return ApiResponse.success(tvProgramEpisodeAdminService.addTvProgramEpisode(request), ApiResponseCode.SUCCESS_200);
    }

    @PostMapping("/episode/update")
    public ApiResponse<TvProgramEpisode> updateTvProgramEpisode(@ModelAttribute UpdateTvProgramEpisodeAdminRequest request) {
        return ApiResponse.success(tvProgramEpisodeAdminService.updateTvProgramEpisode(request), ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/{tvProgramId}/season/{seasonId}/episode")
    public ApiResponse<List<TvProgramEpisode>> findTvProgramEpisodeById(
            @PathVariable String tvProgramId,
            @PathVariable String seasonId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<TvProgramEpisode> episodePage = tvProgramEpisodeAdminService.findTvProgramEpisodeBySeasonId(tvProgramId, seasonId, page, size);
        return ApiResponse.success(
                episodePage.getContent(),
                ApiResponseCode.SUCCESS_200,
                episodePage.getTotalElements(),
                episodePage.getNumber() + 1,
                episodePage.getSize()
        );
    }

    @GetMapping("/episode/publish/{episodeId}")
    public ApiResponse<TvProgramEpisode> publishEpisode(@PathVariable String episodeId) throws IOException {
        return ApiResponse.success(tvProgramEpisodeAdminService.publishTvProgramEpisode(episodeId), ApiResponseCode.SUCCESS_200);
    }

    //TODO remove
    @GetMapping("/episode/updateTranscode/{episodeId}")
    public ApiResponse<Void> updateEpisodeTranscodeResult(@PathVariable String episodeId) throws IOException {

        TranscodeMetaData metaData = new TranscodeMetaData();
        metaData.setMasterPlaylist("");
        metaData.setVariants(new ArrayList<>());

        tvProgramEpisodeAdminService.updateTvProgramEpisodeTranscodeResult(episodeId, metaData);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    // ================================================
    // =========== section
    // =================================================

    @GetMapping("/section/add")
    public ApiResponse<Void> addTvProgramToSection(
            @RequestParam String tvProgramId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop
    ) throws IOException {
        tvProgramAdminService.addTvProgramToSection(tvProgramId, sectionId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/section/remove")
    public ApiResponse<Void> removeTvProgramFromSection(
            @RequestParam Long sectionId,
            @RequestParam String tvProgramId
    ) throws IOException {
        tvProgramAdminService.removeTvProgramFromSection(sectionId, tvProgramId);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }

    @GetMapping("/section/list")
    public ApiResponse<List<TvProgramSectionAdminResponse>> getTvProgramSection(
            @RequestParam Long sectionId,
            @RequestParam(required = false) String isTop,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws IOException {
        List<TvProgramSectionAdminResponse> result = tvProgramAdminService.getTvProgramSection(sectionId, isTop, page, size);

        return ApiResponse.success(
                result,
                ApiResponseCode.SUCCESS_200,
                (long) result.size(),
                page,
                size
        );
    }

    @PutMapping("/section/is-top")
    public ApiResponse<Void> updateTvProgramIsTop(
            @RequestParam Long sectionId,
            @RequestParam String tvProgramId,
            @RequestParam String isTop
    ) throws IOException {
        tvProgramAdminService.updateTvProgramIsTop(sectionId, tvProgramId, isTop);
        return ApiResponse.success(null, ApiResponseCode.SUCCESS_200);
    }
}

